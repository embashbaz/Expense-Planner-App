package com.example.expenseplanner.ui.expenselist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.ui.dialogs.LoginDialog
import com.example.expenseplanner.ui.dialogs.NewCartDialog
import com.example.expenseplanner.ui.dialogs.NoticeDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ExpenseListFragment : Fragment(), LoginDialog.LoginDialogListener {

    lateinit var expenseListRecycler: RecyclerView
    lateinit var addCartFloatingActionButton: FloatingActionButton
    lateinit var expenseListAdapter: ExpenseListAdapter
    lateinit var orderListAdapter: OrderListAdapter
    lateinit var noDataTxt: TextView
    val  expenseListViewModel: ExpenseListViewModel by viewModels{
        ExpenseListViewModelFactory((activity?.application as ExpensePlanner).repository)
    }


    var uId = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_expense_list, container, false)

        uId = ( activity?.application as ExpensePlanner).uId

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
            val bundle = Bundle()
            bundle.putInt("order_passed", 2)
            this.findNavController().navigate(R.id.action_expenseListFragment_to_mapsFragment, bundle)
        }

        if(!uId.isNullOrEmpty())
            setOrderToRecycler()
        else
            openLoginDialog()



    }

    private fun goToCartFromOnline(order: Order) {
        val bundle = Bundle()
        bundle.putParcelable("order", order)
        this.findNavController().navigate(R.id.action_expenseListFragment_to_cartFragment, bundle)


    }

    private fun setOrderToRecycler(){

        expenseListViewModel.getOrderShopProduct(uId)
        expenseListViewModel.orderListProduct.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                val orders = it as ArrayList
                orders.sortWith{ lhs, rhs ->
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    if (lhs.cart?.status!! < rhs.cart!!.status) -1 else if (lhs.cart!!.status > rhs.cart!!.status) 1 else 0
                }
                orderListAdapter.setData(orders)
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

        expenseListViewModel.allCart!!.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                val carts = it as ArrayList
                carts.sortWith{ lhs, rhs ->
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    if (lhs.status!! < rhs.status) -1 else if (lhs.status > rhs.status) 1 else 0
                }
                expenseListAdapter.setData(carts)
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

    fun openNoticeDialog(messageText: String, tag: String){
        val dialog = NoticeDialogFragment(messageText)
        dialog.show(parentFragmentManager, tag)

    }

    fun openLoginDialog(){
        val dialog = LoginDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Please login")

    }

    override fun onLoginBtClick(email: String, password: String) {
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty() ) {
            expenseListViewModel.signUp(email, password)

            expenseListViewModel.loginOutput.observe(viewLifecycleOwner, {

                if (it["status"] == "success") {
                    (activity?.application as ExpensePlanner).uId = it["value"].toString()
                    uId = it["value"].toString()
                    setOrderToRecycler()
                    openNoticeDialog("You have been logged in successfully", "Successfully login")

                }else if(it["status"] == "failed"){
                    openNoticeDialog(it["value"]!!, "Error")
                }
            })
        }
    }

    override fun onRegisterBtClick() {
        openNoticeDialog("Please click on profile to register", "Register")
        this.findNavController().navigateUp()

    }

    override fun onForgotPasswdClick(email: String) {

    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {

    }


}