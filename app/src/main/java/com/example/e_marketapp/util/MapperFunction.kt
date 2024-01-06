package com.example.e_marketapp.util

import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.model.MarketBasketEntity

fun baseModelToMarketBasketEntity(baseModelItem: BaseModelItem): MarketBasketEntity {
    return MarketBasketEntity(
        productId = baseModelItem.id,
        productName = baseModelItem.name,
        singleItemPrice = baseModelItem.price,
        productPrice = baseModelItem.price.toDouble(),
        productCount = 1
    )
}