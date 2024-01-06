package com.example.e_marketapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConventer {
    @TypeConverter
    fun fromHistoryList(historyList: List<HistoryOrderEntity>?): String? {
        return Gson().toJson(historyList)
    }

    @TypeConverter
    fun toHistoryList(historyListString: String?): List<HistoryOrderEntity>? {
        return Gson().fromJson(historyListString, object : TypeToken<List<HistoryOrderEntity>>() {}.type)
    }
}