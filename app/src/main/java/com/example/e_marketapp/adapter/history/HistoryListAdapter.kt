package com.example.e_marketapp.adapter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.HistoryListItemRowBinding
import com.example.e_marketapp.model.HistoryOrderEntity

class HistoryListAdapter : RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder>() {

    private var historyList = mutableListOf<HistoryOrderEntity>()

    fun setHistoryList(list: List<HistoryOrderEntity>) {
        historyList.clear()
        historyList.addAll(mergeDuplicateItems(list))
        notifyDataSetChanged()
    }

    private fun mergeDuplicateItems(list: List<HistoryOrderEntity>): List<HistoryOrderEntity> {
        val resultMap = mutableMapOf<String, HistoryOrderEntity>()

        for (item in list) {
            val key = "${item.productId}_${item.productId}"
            val existingItem = resultMap[key]

            if (existingItem != null) {
                resultMap[key] = existingItem.copy(
                    productCount = existingItem.productCount + item.productCount,
                    productPrice = existingItem.productPrice + item.productPrice
                )
            } else {
                resultMap[key] = item
            }
        }

        return resultMap.values.toList()
    }

    inner class HistoryViewHolder(val binding : HistoryListItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : HistoryOrderEntity){
            binding.apply {
                itemName.text = if (item.productName.length > 10) {
                    "Name :${item.productName.substring(0, 10)}..."
                } else {
                    "Name :${item.productName}"
                }
                itemSinglePrice.text="Price :${item.singleItemPrice}"
                itemTotalPrice.text="Total :${item.productPrice}"
                itemCount.text="Count :${item.productCount}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inf= HistoryListItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryViewHolder(inf)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}