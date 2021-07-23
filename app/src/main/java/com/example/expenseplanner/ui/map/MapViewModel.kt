package com.example.expenseplanner.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.data.Repository
import com.example.expenseplanner.data.ShopKeeper

class MapViewModel : ViewModel(){
    val repository = Repository()

    private var _listShops = MutableLiveData<List<ShopKeeper>>()
    val listShops :LiveData<List<ShopKeeper>>
      get() = _listShops

    private var _loginOutput = MutableLiveData<HashMap<String, String>>()
    val loginOutput: LiveData<HashMap<String, String>>
        get() = _loginOutput

    private var _placeOrderOutput = MutableLiveData<HashMap<String, String>>()
    val placeOrderOutput: LiveData<HashMap<String, String>>
        get() = _placeOrderOutput

    fun signUp (email: String, password: String){
        _loginOutput = repository.login(email, password)
    }

    fun placeOrder(order: Order){
        _placeOrderOutput = repository.placeOrder(order)

    }


    fun getListOfShops(){
        _listShops = repository.getShops()
    }



}