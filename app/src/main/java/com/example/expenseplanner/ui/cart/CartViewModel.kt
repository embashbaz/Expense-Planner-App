package com.example.expenseplanner.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.ItemProduct
import com.example.expenseplanner.data.Repository
import kotlinx.coroutines.launch

class CartViewModel(repository: Repository) : ViewModel() {
    val repository = repository

    fun insertItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.insertItemProduct(item)

    }

    fun deleteItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.deleteItemProduct(item)
    }

    fun updateItemProduct(item: ItemProduct) = viewModelScope.launch{
        repository.updateItemProduct(item)
    }

    fun getItemsForCart(id: Float): LiveData<List<ItemProduct>> {
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