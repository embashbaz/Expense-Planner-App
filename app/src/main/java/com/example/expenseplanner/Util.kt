package com.example.expenseplanner

import java.text.SimpleDateFormat
import java.util.*
import javax.annotation.meta.When

fun getDate(): String{
    val sdf = SimpleDateFormat("dd/M/yyyy")
    return sdf.format(Date()).toString()
}

fun showStatusValue(code: Int): String{

    when(code){
        1 -> return "Active Cart"
        2 -> return "Order Placed and will be collected"
        3 -> return "Order Placed and will be delivered"
        4 -> return "Order has been cancelled"
        5 -> return "Order has been received"
        6 -> return "Order has been updated by the shop keeper"
        7 -> return "Order has been updated by the client"
        8 -> return "Order has been confirmed by the client"
        9 -> return "Order is ready"
        10 -> return "Order is on the way to being delivered"
        11 -> return "Order has been collected"

    }

    return "Error"

}