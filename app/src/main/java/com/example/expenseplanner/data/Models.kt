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
    var name: String,
    val date: String,
    var price: Double,
    var quantity: Double,
    var totalPriceNum: Double,
    var description: String


)