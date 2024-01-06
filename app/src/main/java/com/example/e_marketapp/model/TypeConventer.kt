package com.example.e_marketapp.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConventer {
    @TypeConverter
    fun fromHistoryList(historyList: ArrayList<HistoryOrderEntity>?): String? {
        return Gson().toJson(historyList)
    }

    @TypeConverter
    fun toHistoryList(historyListString: String?): ArrayList<HistoryOrderEntity>? {
        return Gson().fromJson(historyListString, object : TypeToken<ArrayList<HistoryOrderEntity>>() {}.type)
    }
}