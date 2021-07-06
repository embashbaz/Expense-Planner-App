package com.example.expenseplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: String,
    val status: Int,
    val dateCreated: String,
    val totalPrice: Double,

)

@Entity
data class ItemProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cartId: Int,
    val name: String,
    val date: String,
    val price: Double,
    val quantity: Double,
    val totalPriceNum: Double,
    val description: String


)