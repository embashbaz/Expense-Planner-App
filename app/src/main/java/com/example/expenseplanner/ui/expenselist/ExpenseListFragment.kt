package com.example.expenseplanner.ui.expenselist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.ui.dialogs.NewCartDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ExpenseListFragment : Fragment() {

    lateinit var expenseListRecycler: RecyclerView
    lateinit var addCartFloatingActionButton: FloatingActionButton
    lateinit var expenseListAdapter: ExpenseListAdapter
    lateinit var orderListAdapter: OrderListAdapter
    lateinit var noDataTxt: TextView
    val  expenseListViewModel: ExpenseListViewModel by viewModels{
        ExpenseListViewModelFactory((activity?.application as ExpensePlanner).repository)
    }


    val uId : String by lazy {  ( activity?.application as ExpensePlanner).uId }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_expense_list, container, false)

        expenseListRecycler = view.findViewById(R.id.expense_list_recycler)
        addCartFloatingActionButton = view.findViewById(R.id.add_cart_button)
        noDataTxt = view.findViewById(R.id.no_data_expense_list)

        if(requireArguments().getInt("code")  == 1){
            showExpenseListData()
        }else if (requireArguments().getInt("code")  == 2){
            showOrderList()
        }


        return  view;
    }

    private fun showExpenseListData(){


        expenseListAdapter = ExpenseListAdapter { cart -> goToCart(cart) }

        addCartFloatingActionButton.setOnClickListener {
            createCart()
        }

        setDatatoRecycler()
    }

    private fun showOrderList(){

        orderListAdapter = OrderListAdapter { order -> goToCartFromOnline(order) }

        addCartFloatingActionButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_expenseListFragment_to_mapsFragment)
        }

        if(!uId.isNullOrEmpty())
            setOrderToRecycler()



    }

    private fun goToCartFromOnline(order: Order) {
        val bundle = Bundle()
        bundle.putParcelable("order", order)
        this.findNavController().navigate(R.id.action_expenseListFragment_to_cartFragment)


    }

    private fun setOrderToRecycler(){

        expenseListViewModel.getOrderShopProduct(uId)
        expenseListViewModel.orderListProduct.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                orderListAdapter.setData(it as ArrayList<Order>)
                expenseListRecycler.visibility = View.VISIBLE
                noDataTxt.visibility = View.INVISIBLE
            }else{
                expenseListRecycler.visibility = View.INVISIBLE
                noDataTxt.visibility = View.VISIBLE
            }


        })
        expenseListRecycler.layoutManager = LinearLayoutManager(activity)
        expenseListRecycler.adapter = orderListAdapter
    }



    private fun setDatatoRecycler(){

        expenseListViewModel.allCart.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                expenseListAdapter.setData(it as ArrayList<Cart>)
                expenseListRecycler.visibility = View.VISIBLE
                noDataTxt.visibility = View.INVISIBLE
            }else{
                expenseListRecycler.visibility = View.INVISIBLE
                noDataTxt.visibility = View.VISIBLE
            }


        })
        expenseListRecycler.layoutManager = LinearLayoutManager(activity)
        expenseListRecycler.adapter = expenseListAdapter
    }

    private fun goToCart(cart: Cart) {
        val bundle = Bundle()
        bundle.putParcelable("cart", cart)
        this.findNavController().navigate(R.id.action_expenseListFragment_to_cartFragment, bundle)
    }

    private fun createCart() {
        val newCartDialog = NewCartDialog(expenseListViewModel)
        newCartDialog.show(parentFragmentManager, "create new Cart")
    }


}