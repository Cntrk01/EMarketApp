package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.HistoryListItemRowBinding
import com.example.e_marketapp.databinding.ProfileListItemRowBinding
import com.example.e_marketapp.model.HistoryOrderModel

class ProfileAdapter(
    private val clickHistoryItem : ((HistoryOrderModel)->Unit)?=null
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>(){

    private var historyList = emptyList<HistoryOrderModel>()

    fun setHistoryList(list : List<HistoryOrderModel>){
        this.historyList=list
        notifyDataSetChanged()
    }

    inner class ProfileViewHolder(val binding : ProfileListItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(item:HistoryOrderModel){
            binding.historyCount.text="History : ${item.id.toString()}"

            itemView.setOnClickListener {
                clickHistoryItem?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inf=ProfileListItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProfileViewHolder(inf)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
       return historyList.size
    }
}