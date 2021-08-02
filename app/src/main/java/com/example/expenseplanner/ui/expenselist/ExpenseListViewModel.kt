package com.example.expenseplanner.ui.expenselist

import androidx.lifecycle.*
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.data.Repository
import kotlinx.coroutines.launch

class ExpenseListViewModel(repository: Repository) : ViewModel() {
   val repository = repository

    val allCart = repository.allCarts

    private var _orderListProduct = MutableLiveData<List<Order>>()
    val orderListProduct : LiveData<List<Order>>
        get() = _orderListProduct

    private var _loginOutput = MutableLiveData<HashMap<String, String>>()
    val loginOutput: LiveData<HashMap<String, String>>
        get() = _loginOutput

    fun signUp (email: String, password: String){
        _loginOutput = repository.login(email, password)
    }

    fun getOrderShopProduct(uId: String){
        _orderListProduct = repository.getShopOrders(uId)
    }

    fun insertCart(cart: Cart) = viewModelScope.launch{
        repository.insertExpense(cart)
    }



}

class ExpenseListViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}