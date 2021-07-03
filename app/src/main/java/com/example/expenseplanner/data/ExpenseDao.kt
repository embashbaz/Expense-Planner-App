package com.example.expenseplanner.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface ExpenseDao {

    @Insert
    fun insertExpense(cart: Cart)

    @Insert
    fun insertItemProduct(item: ItemProduct)



}