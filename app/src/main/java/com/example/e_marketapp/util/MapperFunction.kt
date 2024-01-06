package com.example.e_marketapp.util

import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.model.HistoryOrderEntity
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.model.MarketEntity

fun baseModelToMarketBasketEntity(baseModelItem: BaseModelItem): MarketBasketEntity {
    return MarketBasketEntity(
        productId = baseModelItem.id,
        productName = baseModelItem.name,
        singleItemPrice = baseModelItem.price,
        productPrice = baseModelItem.price.toDouble(),
        productCount = 1
    )
}

fun baseModelItemToMarketEntity(baseModelItem: BaseModelItem): MarketEntity {
    return MarketEntity(
        marketId = baseModelItem.id,
        brand = baseModelItem.brand,
        createdAt = baseModelItem.createdAt,
        description = baseModelItem.description,
        image = baseModelItem.image,
        model = baseModelItem.model,
        name = baseModelItem.name,
        price = baseModelItem.price
    )
}

fun mapBasketToHistoryOrder(basketList: List<MarketBasketEntity>?): List<HistoryOrderEntity> {
    val historyOrderMap = mutableMapOf<String, HistoryOrderEntity>()

    basketList?.let {
        for (basketItem in it) {
            val existingHistoryOrderItem = historyOrderMap[basketItem.productId]

            if (existingHistoryOrderItem != null) {
                if (basketItem.productCount > existingHistoryOrderItem.productCount) {
                    historyOrderMap[basketItem.productId] = existingHistoryOrderItem.copy(
                        productCount = basketItem.productCount,
                        productPrice = basketItem.productPrice * basketItem.productCount
                    )
                }
            } else {
                val newHistoryOrderItem = HistoryOrderEntity(
                    productId = basketItem.productId,
                    productName = basketItem.productName,
                    singleItemPrice = basketItem.singleItemPrice,
                    productPrice = basketItem.productPrice * basketItem.productCount,
                    productCount = basketItem.productCount
                )
                historyOrderMap[basketItem.productId] = newHistoryOrderItem
            }
        }
    }
    return historyOrderMap.values.toList()
}