package com.example.expenseplanner.ui.cart

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.showStatusValue
import com.example.expenseplanner.ui.dialogs.CheckoutDialog
import com.example.expenseplanner.ui.dialogs.ItemDialog
import com.example.expenseplanner.ui.dialogs.LoginDialog
import com.example.expenseplanner.ui.dialogs.NoticeDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CartFragment : Fragment(), ItemDialog.BackToCartOrder, CheckoutDialog.CheckoutInterface, LoginDialog.LoginDialogListener {

    lateinit var typeCart: TextView
    lateinit var dateCreated: TextView
    lateinit var statusCart: TextView
    lateinit var totalCart: TextView
    lateinit var recyclerCart: RecyclerView
    lateinit var addItem: FloatingActionButton
    lateinit var noData: TextView
    lateinit var checkOutButton: Button
    lateinit var deleteCartMenu : MenuItem

    lateinit var cartAdapter: CartAdapter
    var cart: Cart? = null
    var order: Order? = null
    var actionCode = 0
    var productList: List<ItemProduct>? = null
    var uId = ""


    val  cartViewModel: CartViewModel by viewModels{
        CartViewModelFactory((activity?.application as ExpensePlanner).repository)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        initView(view)
        cartAdapter = CartAdapter { itemProduct -> itemDetail(itemProduct) }
        uId = ( activity?.application as ExpensePlanner).uId

        if(arguments?.getParcelable<Cart>("cart") != null) {
            cart = arguments?.getParcelable("cart")!!
            populateViews()
            if(cart!!.status != 1){
                disableButtons()
            }

            checkOutButton.setOnClickListener{
               onCheckoutButtonPressed()
            }

        }
        else if (arguments?.getParcelable<Order>("order") != null){
            order = arguments?.getParcelable("order")
            addItem.isEnabled = false
            populateViewsFromOrder()
            actionCode = 5
        }

        if(actionCode == 5){
            if (order!!.cart?.status != 1){
                disableButtons()
            }

        }


        addItem.setOnClickListener{
            onAddItemsPressed()
        }


        return view
    }

    fun disableButtons(){

        addItem.isEnabled = false
        checkOutButton.isEnabled = false
        deleteCartMenu.isVisible = false
    }

    fun onAddItemsPressed(){
        if(!cart?.shopKey.isNullOrEmpty()){
            val bundle = Bundle()
            cart?.id?.let { bundle.putInt("cartId", it) }
            this.findNavController().navigate(R.id.action_cartFragment_to_mapsFragment, bundle)


        }else{

            val newItem = cart?.id?.let { it1 -> ItemDialog(1,cartViewModel, it1, null) }
            newItem!!.show(parentFragmentManager, "Add new product")
        }


    }

    fun onCheckoutButtonPressed(){
        val dialog = CheckoutDialog()
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Choose checkout options")
    }

    override fun checkoutOptionCode(code: Int) {
       if(!productList!!.isEmpty()) {
        if(code == 0){
            cart!!.status = 11
            cartViewModel.updateCart(cart!!)
            disableButtons()
        }else if(code == 1 || code == 2){
            if(code == 1)
            cart!!.status = 2
            else
                cart!!.status = 3
            order  = Order(cart = cart,itemList = productList)

            if(!cart!!.shopKey.isNullOrEmpty()){
                checkUserLogin()
            }else{
                passOrderToMap(order!!)
            }

        }
       }else{


       }
    }

    fun checkUserLogin(){
        order!!.shopId = cart!!.shopKey

        if(!uId.isNullOrEmpty()){
             getNamesWithId()
        }else{
            openLoginDialog()
        }
    }

    fun getNamesWithId(){
        cartViewModel.getShopData(cart!!.shopKey)
        cartViewModel.getUserData(uId)
        cartViewModel.shopData.observe(viewLifecycleOwner, {
            if(it!= null){
                order!!.shopName = it.name
                order!!.userId = uId
                order!!.shopToken = it.msgToken
                cartViewModel.userData.observe(viewLifecycleOwner,{
                    if(it != null){
                        order!!.userName = it.name
                        order!!.userToken = it.msgToken
                        sendOrder()
                    }
                })
            }

        })
    }

    fun sendOrder(){


        cartViewModel.placeOrder(order!!)
        cartViewModel.placeOrderOutput.observe(viewLifecycleOwner, {
            if(it["status"] == "success"){
                openNoticeDialog("Your order has been placed", "success")
                cartViewModel.updateCart(cart!!)
                disableButtons()

            }else if(it["status"] == "failed"){
                openNoticeDialog(it["value"]!!, "Error Placing order")
            }
        })

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
            cartViewModel.login(email, password)

            cartViewModel.loginOutput.observe(viewLifecycleOwner, {
                Toast.makeText(activity, it["status"] + ": " + it["value"], Toast.LENGTH_LONG)
                    .show()
                if (it["status"] == "success") {
                    (activity?.application as ExpensePlanner).uId = it["value"].toString()
                    uId = it["value"].toString()
                    Toast.makeText(activity,"You have been logged in successfully", Toast.LENGTH_SHORT).show()
                    getNamesWithId()
                }else if(it["status"] == "failed"){
                    openNoticeDialog(it["value"]!!, "Error")
                }
            })
        }
    }

    fun passOrderToMap(order: Order){
        val bundle = Bundle()
        bundle.putInt("order_passed", 1)
        bundle.putParcelable("order", order)
        this.findNavController().navigate(R.id.action_cartFragment_to_mapsFragment, bundle)
    }




    private fun populateViewsFromOrder() {

        if(!order!!.itemList?.isEmpty()!!){
            cartAdapter.setData(order!!.itemList as ArrayList<ItemProduct>)
           calculateToatal(order!!.itemList as java.util.ArrayList<ItemProduct>)
            recyclerCart.visibility = View.VISIBLE
            noData.visibility = View.INVISIBLE
        }else{
            recyclerCart.visibility = View.INVISIBLE
            noData.visibility = View.VISIBLE
        }

    dateCreated.text = order!!.cart?.dateCreated
    typeCart.text = order!!.cart?.type
    statusCart.text = order!!.cart?.status?.let { showStatusValue(it) }
    recyclerCart.layoutManager = LinearLayoutManager(activity)
    recyclerCart.adapter = cartAdapter

    }

    private fun itemDetail(itemProduct: ItemProduct) {
        if(cart?.status != 1) {
            var itemDialog: ItemDialog
            if (!cart?.shopKey.isNullOrEmpty())
                itemDialog = cart?.let { ItemDialog(2, cartViewModel, it.id, itemProduct) }!!
            else if (actionCode == 5)
            Toast.makeText(activity, "you have place the order for this cart", Toast.LENGTH_SHORT).show()
            //itemDialog =cart?.id?.let { ItemDialog(5, null, 0, itemProduct) }!!
            else {itemDialog = cart?.id?.let { ItemDialog(4, cartViewModel, it, itemProduct) }!!
                  itemDialog.show(parentFragmentManager, "Update product")}
        }



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
                    productList = it
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
        statusCart.text = showStatusValue(cart!!.status)
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
        order!!.itemList?.let { getItemImpl(it, itemProduct) }?.let { order!!.itemList?.drop(it) }
        order!!.cart?.status = 7
    }

    override fun updateItemOrder(itemProduct: ItemProduct) {
        var items: MutableList<ItemProduct> = order!!.itemList as MutableList<ItemProduct>
        items[order!!.itemList?.let { getItemImpl(it, itemProduct) }!!] = itemProduct
        order!!.itemList = items
        order!!.cart?.status  = 7
    }

    private fun getItemImpl(list: List<ItemProduct>, item: ItemProduct): Int {
        list.forEachIndexed { index, it ->
            if (it == item)
                return index
        }
        return -1
    }



    override fun onRegisterBtClick() {
        openNoticeDialog("Please go to the landin screen then click on profile to Register", "Request to Register")
    }

    override fun onForgotPasswdClick(email: String) {
        TODO("Not yet implemented")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
       dialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.cart_menu, menu)
        deleteCartMenu = menu.findItem(R.id.delete_cart)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.delete_cart){
            cartViewModel.deleteCart(cart!!)
            this.findNavController().navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }


}