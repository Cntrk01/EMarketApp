package com.example.e_marketapp.model

data class HistoryOrderEntity(
    val productId: String,
    val productName: String,
    val singleItemPrice: String,
    var productPrice: Double,
    var productCount: Int
)