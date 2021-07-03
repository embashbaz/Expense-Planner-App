package com.example.expenseplanner.ui.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.expenseplanner.data.Cart
import com.example.expenseplanner.data.Repository
import kotlinx.coroutines.launch

class ExpenseListViewModel(repository: Repository) : ViewModel() {
   val repository = repository

    val allCart = repository.allCarts
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