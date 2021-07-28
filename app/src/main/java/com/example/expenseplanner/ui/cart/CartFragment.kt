package com.example.expenseplanner.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.ui.dialogs.ItemDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CartFragment : Fragment(), ItemDialog.BackToCartOrder {

    lateinit var typeCart: TextView
    lateinit var dateCreated: TextView
    lateinit var statusCart: TextView
    lateinit var totalCart: TextView
    lateinit var recyclerCart: RecyclerView
    lateinit var addItem: FloatingActionButton
    lateinit var noData: TextView
    lateinit var checkOutButton: Button

    lateinit var cartAdapter: CartAdapter
    var cart: Cart? = null
    var order: Order? = null
    var actionCode = 0


    val  cartViewModel: CartViewModel by viewModels{
        CartViewModelFactory((activity?.application as ExpensePlanner).repository)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        initView(view)

        if(arguments?.getParcelable<Cart>("cart") != null) {
            cart = arguments?.getParcelable("cart")!!
            populateViews()

        }
        else if (arguments?.getParcelable<Order>("order") != null){
            order = arguments?.getParcelable("order")
            addItem.isEnabled = false
            populateViewsFromOrder()
            actionCode = 5
        }

        if(actionCode == 5){
            if (order!!.cart.status != 4){
                checkOutButton.isEnabled = false
            }else{


            }

        }



        cartAdapter = CartAdapter { itemProduct -> itemDetail(itemProduct) }


        addItem.setOnClickListener{
            val newItem = cart?.id?.let { it1 -> ItemDialog(1,cartViewModel, it1, null) }
            newItem!!.show(parentFragmentManager, "Add new product")
        }


        return view
    }

    private fun populateViewsFromOrder() {

        if(!order!!.itemList.isEmpty()){
            cartAdapter.setData(order!!.itemList as ArrayList<ItemProduct>)
           calculateToatal(order!!.itemList as java.util.ArrayList<ItemProduct>)
            recyclerCart.visibility = View.VISIBLE
            noData.visibility = View.INVISIBLE
        }else{
            recyclerCart.visibility = View.INVISIBLE
            noData.visibility = View.VISIBLE
        }

    dateCreated.text = order!!.cart.dateCreated
    typeCart.text = order!!.cart.type
    statusCart.text = order!!.cart.status.toString()
    recyclerCart.layoutManager = LinearLayoutManager(activity)
    recyclerCart.adapter = cartAdapter

    }

    private fun itemDetail(itemProduct: ItemProduct) {
        var itemDialog: ItemDialog

        if(!cart?.shopKey.isNullOrEmpty())
        itemDialog = cart?.let { ItemDialog(2,cartViewModel, it.id,  itemProduct) }!!
        else if (actionCode == 5)  itemDialog = cart?.id?.let { ItemDialog(5,null, 0,  itemProduct) }!!
        else itemDialog = cart?.id?.let { ItemDialog(4,cartViewModel, it,  itemProduct) }!!

        itemDialog.show(parentFragmentManager, "Update product")

    }

    private fun initView(view: View){
        typeCart = view.findViewById(R.id.cart_type)
        dateCreated = view.findViewById(R.id.date_created_expense)
        statusCart = view.findViewById(R.id.cart_status)
        totalCart = view.findViewById(R.id.cart_total)
        recyclerCart = view.findViewById(R.id.cart_recyclerView)
        addItem = view.findViewById(R.id.add_item_cart)
        noData = view.findViewById(R.id.no_data_cart)
        checkOutButton = view.findViewById(R.id.check_out_button)

    }

    private fun populateViews(){

        cart?.id?.let {
            cartViewModel.getItemsForCart(it).observe(viewLifecycleOwner, {
                if(!it.isEmpty()){
                    cartAdapter.setData(it as ArrayList<ItemProduct>)
                    calculateToatal(it)
                    recyclerCart.visibility = View.VISIBLE
                    noData.visibility = View.INVISIBLE
                }else{
                    recyclerCart.visibility = View.INVISIBLE
                    noData.visibility = View.VISIBLE
                }


            })
        }

        dateCreated.text = cart?.dateCreated
        typeCart.text = cart?.type
        statusCart.text = cart?.status.toString()
        recyclerCart.layoutManager = LinearLayoutManager(activity)
        recyclerCart.adapter = cartAdapter



    }

    private fun calculateToatal(it: java.util.ArrayList<ItemProduct>) {
        var sum = 0.0
        for(item in it ){
            sum = sum + item.totalPriceNum

        }

        totalCart.text = "ksh"+sum
        cart?.totalPrice = sum
        cart?.let { it1 -> cartViewModel.updateCart(it1) }
    }

    override fun deleteItemOrder(itemProduct: ItemProduct) {
        order!!.itemList.drop(getItemImpl(order!!.itemList, itemProduct))
        order!!.cart.status = 5
    }

    override fun updateItemOrder(itemProduct: ItemProduct) {
        var items: MutableList<ItemProduct> = order!!.itemList as MutableList<ItemProduct>
        items[getItemImpl(order!!.itemList, itemProduct)] = itemProduct
        order!!.itemList = items
        order!!.cart.status = 5
    }

    private fun getItemImpl(list: List<ItemProduct>, item: ItemProduct): Int {
        list.forEachIndexed { index, it ->
            if (it == item)
                return index
        }
        return -1
    }

}