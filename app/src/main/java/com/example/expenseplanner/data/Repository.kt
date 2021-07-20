package com.example.expenseplanner.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow


class Repository(private val expenseDao: ExpenseDao) {

    var mFirebaseAuth: FirebaseAuth
    var mFirebaseDb : FirebaseFirestore

    init {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mFirebaseDb = FirebaseFirestore.getInstance()

    }

    val allCarts = expenseDao.getAllCart()

    fun getItemsForCart(id: Int): LiveData<List<ItemProduct>>{
        return expenseDao.getAllItemForCart(id)
    }

    @WorkerThread
    suspend fun insertExpense(cart: Cart){
        expenseDao.insertExpense(cart)
    }

    @WorkerThread
    suspend fun insertItemProduct(item: ItemProduct){
        expenseDao.insertItemProduct(item)
    }

    @WorkerThread
    suspend fun updateCart(cart: Cart){
        expenseDao.updateCart(cart)
    }

    @WorkerThread
    suspend fun updateItemProduct(item: ItemProduct){
        expenseDao.updateItemProduct(item)
    }

    @WorkerThread
    suspend fun deleteCart(cart: Cart){
        expenseDao.deleteCart(cart)
        expenseDao.deleteAllItemForCart(cart.id)
    }

    @WorkerThread
    suspend fun deleteItemProduct(item: ItemProduct){
        expenseDao.deleteItemProduct(item)
    }



}