package com.example.expenseplanner

import android.app.Application
import com.example.expenseplanner.data.ExpenseDb
import com.example.expenseplanner.data.Order
import com.example.expenseplanner.data.Repository

class ExpensePlanner: Application() {

    var uId = ""
    var order: Order? = null

    val database: ExpenseDb by lazy {
        ExpenseDb.getDatabase(this)
    }

    val repository: Repository by lazy {
        Repository(database.expenseDao())
    }
}