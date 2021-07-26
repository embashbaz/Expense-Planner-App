package com.example.expenseplanner.ui.productlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenseplanner.R
import com.example.expenseplanner.data.ShopProduct


class ProductListFragment : Fragment() {

    lateinit var productListAdapter: ProductListAdapter
    lateinit var productsRecyclerView: RecyclerView
    lateinit var noDataTxt: TextView
    var shopId = ""

    val productListViewModel: ShopProductListViewModel by lazy {
        ViewModelProvider(this).get(ShopProductListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_product_list, container, false)

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

    }


}