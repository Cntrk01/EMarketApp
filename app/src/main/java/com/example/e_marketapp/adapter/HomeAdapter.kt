package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_marketapp.databinding.HomeItemRowBinding
import com.example.e_marketapp.model.BaseModelItem

class HomeAdapter :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val marketModelList = arrayListOf<BaseModelItem>()

    fun addMoreData(newData: List<BaseModelItem>) {
        marketModelList.clear()
        marketModelList.addAll(newData)
        notifyDataSetChanged()
        println("ADAPTER LİST SİZE : ${marketModelList.size}")
    }

    inner class ViewHolder(val binding: HomeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(marketItem: BaseModelItem) {
            binding.apply {
                Glide.with(binding.root).load(marketItem.image).into(marketItemImage)
                marketItemPrice.text = marketItem.price + "$"
                marketItemName.text = marketItem.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inf = HomeItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inf)
    }

    override fun getItemCount(): Int {
        return marketModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(marketItem = marketModelList[position])
    }
}