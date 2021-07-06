package com.example.expenseplanner.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.ui.dialogs.ItemDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CartFragment : Fragment() {

    lateinit var typeCart: TextView
    lateinit var dateCreated: TextView
    lateinit var statusCart: TextView
    lateinit var totalCart: TextView
    lateinit var recyclerCart: TextView
    lateinit var addItem: FloatingActionButton
    lateinit var noData: TextView

    lateinit var cartAdapter: CartAdapter
    lateinit var cart: Cart

    val  cartViewModel: CartViewModel by viewModels{
        CartViewModelFactory((activity?.application as ExpensePlanner).repository)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_cart, container, false)
        initView(view)
        cartAdapter = CartAdapter { itemProduct -> itemDetail(itemProduct) }
        populateViews()

        addItem.setOnClickListener{
            val newItem = ItemDialog(1,cartViewModel, cart.id, null)
            newItem.show(parentFragmentManager, "Add new product")
        }


        return view
    }

    private fun itemDetail(itemProduct: ItemProduct) {
        val newItem = ItemDialog(2,cartViewModel,cart.id,  itemProduct)
        newItem.show(parentFragmentManager, "Update product")

    }

    private fun initView(view: View){
        typeCart = view.findViewById(R.id.cart_type)
        dateCreated = view.findViewById(R.id.date_created_expense)
        statusCart = view.findViewById(R.id.cart_status)
        totalCart = view.findViewById(R.id.cart_total)
        recyclerCart = view.findViewById(R.id.cart_recyclerView)
        addItem = view.findViewById(R.id.add_item_cart)
        noData = view.findViewById(R.id.no_data_cart)

    }

    private fun populateViews(){

        cartViewModel.getItemsForCart(cart.id).observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                cartAdapter.setData(it as ArrayList<ItemProduct>)
                recyclerCart.visibility = View.VISIBLE
                noData.visibility = View.INVISIBLE
            }else{
                recyclerCart.visibility = View.INVISIBLE
                noData.visibility = View.VISIBLE
            }


        })

        dateCreated.text = cart.dateCreated
        typeCart.text = cart.type
        //TODO: Calculate total and ....


        recyclerCart.layoutManager = LinearLayoutManager(activity)
        recyclerCart.adapter = cartAdapter

    }

}