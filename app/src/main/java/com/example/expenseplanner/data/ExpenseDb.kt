package com.example.expenseplanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database (entities = arrayOf(Cart::class, ItemProduct::class), version = 2, exportSchema = false)
abstract class ExpenseDb : RoomDatabase(){

    abstract fun expenseDao(): ExpenseDao

    companion object{

        @Volatile
        private var INSTANCE: ExpenseDb? = null
        fun getDatabase(context: Context): ExpenseDb {

            var instance = INSTANCE

            if(instance == null) {

                 instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDb::class.java,
                    "expense_db"
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
            }
               return instance

        }


    }

}