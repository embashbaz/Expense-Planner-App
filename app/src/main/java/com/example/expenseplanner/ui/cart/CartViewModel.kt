package com.example.expenseplanner.ui.cart

import androidx.lifecycle.*
import com.example.expenseplanner.data.*
import kotlinx.coroutines.launch

class CartViewModel(repository: Repository) : ViewModel() {
    val repository = repository

    var userData = MutableLiveData<GeneralUser?>()

    var shopData = MutableLiveData<ShopKeeper?>()


    private var _placeOrderOutput = MutableLiveData<HashMap<String, String>>()
    val placeOrderOutput: LiveData<HashMap<String, String>>
        get() = _placeOrderOutput

    private var _loginOutput = MutableLiveData<HashMap<String, String>>()
    val loginOutput: LiveData<HashMap<String, String>>
        get() = _loginOutput

    fun login (email: String, password: String){
        _loginOutput = repository.login(email, password)
    }

    fun placeOrder(order: Order){
        _placeOrderOutput = repository.placeOrder(order)

    }

    fun getShopData(uId: String){
        shopData = repository.getShop(uId)
    }

    fun getUserData(uId: String){
        userData = repository.getUser(uId)
    }

    fun insertItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.insertItemProduct(item)

    }

    fun deleteItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.deleteItemProduct(item)
    }

    fun deleteCart(cart : Cart) = viewModelScope.launch{
        repository.deleteCart(cart)
    }

    fun updateItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.updateItemProduct(item)
    }

    fun updateCart(cart: Cart) = viewModelScope.launch{
       repository.updateCart(cart)
    }

    fun getItemsForCart(id: Int): LiveData<List<ItemProduct>> {
        return repository.getItemsForCart(id)
    }




}

class CartViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}