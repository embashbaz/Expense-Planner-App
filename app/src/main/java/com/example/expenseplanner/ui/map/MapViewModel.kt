package com.example.expenseplanner.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseplanner.data.Repository
import com.example.expenseplanner.data.ShopKeeper

class MapViewModel : ViewModel(){
    val repository = Repository()

    var _listShops = MutableLiveData<List<ShopKeeper>>()
    val listShops :LiveData<List<ShopKeeper>>
      get() = _listShops


    fun getListOfShops(){
        _listShops = repository.getShops()
    }



}