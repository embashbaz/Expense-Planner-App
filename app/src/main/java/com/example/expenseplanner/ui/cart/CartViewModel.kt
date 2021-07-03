package com.example.expenseplanner.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.Repository
import kotlinx.coroutines.launch

class CartViewModel(repository: Repository) : ViewModel() {

    fun insertCart(cart: Cart) = viewModelScope.launch{

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