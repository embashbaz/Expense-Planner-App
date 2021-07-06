package com.example.expenseplanner.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(cart: Cart)

    @Insert
    suspend fun insertItemProduct(item: ItemProduct)

    @Update
    suspend fun updateCart(cart: Cart)

    @Update
    suspend fun updateItemProduct(item: ItemProduct)

    @Delete
    suspend fun deleteCart(cart: Cart)

    @Delete
    suspend fun deleteItemProduct(item: ItemProduct)

    @Query("SELECT * FROM Cart ORDER BY id")
    fun getAllCart(): LiveData<List<Cart>>

    @Query("SELECT * FROM Itemproduct WHERE cartId =:id")
    fun getAllItemForCart(id: Int): LiveData<List<ItemProduct>>

    @Query("DELETE FROM Itemproduct WHERE cartId =:id")
    suspend fun deleteAllItemForCart(id: Int)



}