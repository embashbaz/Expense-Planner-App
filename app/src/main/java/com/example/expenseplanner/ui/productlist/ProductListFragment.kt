package com.example.expenseplanner.ui.productlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.ExpensePlanner
import com.example.expenseplanner.R
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.data.ShopProduct
import com.example.expenseplanner.getDate
import com.example.expenseplanner.ui.dialogs.ItemDialog


class ProductListFragment : Fragment(), ItemDialog.BackToShopListDialogListener {

    lateinit var productListAdapter: ProductListAdapter
    lateinit var productsRecyclerView: RecyclerView
    lateinit var noDataTxt: TextView
    var shopId = ""
    var passedCartId = -1
    var passedShopName = ""

    val  productListViewModel: ShopProductListViewModel by viewModels{
        ShopProductListViewModelFactory((activity?.application as ExpensePlanner).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_product_list, container, false)

        shopId = arguments?.getString("shopId").toString()
        passedShopName =arguments?.getString("shopName").toString()


        productsRecyclerView = view.findViewById(R.id.product_list_recycler)
        noDataTxt = view.findViewById(R.id.no_data_txt)


        productListAdapter = ProductListAdapter { shopProduct -> openItemDialog(shopProduct) }

        productListViewModel.getListShopProduct(shopId)
        productListViewModel.shopListProduct.observe(viewLifecycleOwner, {
            if(!it.isEmpty()){
                productListAdapter.setData(it as ArrayList<ShopProduct>)
                productsRecyclerView.visibility = View.VISIBLE
                noDataTxt.visibility = View.INVISIBLE
            }else{
                productsRecyclerView.visibility = View.INVISIBLE
                noDataTxt.visibility = View.VISIBLE
            }
        })

        productsRecyclerView.layoutManager = LinearLayoutManager(activity)
        productsRecyclerView.adapter = productListAdapter


        return view
    }

    private fun openItemDialog(shopProduct: ShopProduct) {

        val itemProduct = ItemProduct(0,0,shopProduct.productName,"",shopProduct.price, 0.0,0.0,shopProduct.description)
        val itemDialog = ItemDialog(3,null,0,  itemProduct)
        itemDialog.setListener(this)
        itemDialog.show(parentFragmentManager, "Update product")

    }

    override fun onAddShopProduct(itemProduct: ItemProduct) {
        if(passedCartId > -1){
            itemProduct.cartId = passedCartId
            saveNewItemToCart(itemProduct)

        }else{
            checkActiveCart(itemProduct)
        }

    }

    private fun checkActiveCart(itemProduct: ItemProduct){

        productListViewModel.getActiveCartIdIfExist(1, shopId).observe(viewLifecycleOwner, {
                if(it.isEmpty()){
                    val cart = Cart(0, passedShopName,1, getDate(), 0.0,shopId)
                    productListViewModel.insertCart(cart)
                    //productListViewModel.newCartId?.observe(viewLifecycleOwner, {
                //        itemProduct.cartId = it.toInt()
               //         saveNewItemToCart(itemProduct)
               //     })

                }else{
                    itemProduct.cartId = it[0].id
                    saveNewItemToCart(itemProduct)
                }
        })


    }

    private fun saveNewItemToCart(itemProduct: ItemProduct){
        productListViewModel.insertItemProduct(itemProduct)
    }


}