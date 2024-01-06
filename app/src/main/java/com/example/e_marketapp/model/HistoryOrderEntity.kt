package com.example.e_marketapp.model

data class HistoryOrderEntity(
    val productId: String,
    val productName: String,
    val singleItemPrice: Int,
    var productPrice: Int,
    var productCount: Int
)