package com.example.expenseplanner.ui.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseplanner.data.Repository
import com.example.expenseplanner.data.ShopProduct

class ShopProductListViewModel: ViewModel() {
    val repository = Repository()

    private var _shopListProduct = MutableLiveData<List<ShopProduct>>()
    val shopListProduct : LiveData<List<ShopProduct>>
        get() = _shopListProduct

    fun getListShopProduct(uId: String){
        _shopListProduct = repository.getShopProducts(uId)
    }

}