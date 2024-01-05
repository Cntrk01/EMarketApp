package com.example.e_marketapp.adapter.filter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.FilterPageItemRowBinding
import com.example.e_marketapp.model.FilterModelItem

class SortByAdapter  (private val selectedItem : ((FilterModelItem)->Unit) ?=null
): RecyclerView.Adapter<SortByAdapter.SortByViewHolder>() {

    private var modelList = emptyList<FilterModelItem>()
    private var selectedPosition = -1

    @SuppressLint("NotifyDataSetChanged")
    fun setModelList(list: List<FilterModelItem>) {
        this.modelList = list
        notifyDataSetChanged()
    }

    inner class SortByViewHolder(val binding: FilterPageItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilterModelItem) {
            binding.apply {
                checkBox.setBackgroundResource(R.drawable.checkbox_selector)
                checkBox.isSelected = item.checkBoxValue
                checkText.text = item.title

                checkBox.setOnClickListener {
                    if (!item.checkBoxValue) {
                        val previousPosition = selectedPosition
                        if (previousPosition != RecyclerView.NO_POSITION) {
                            modelList[previousPosition].checkBoxValue = false
                            notifyItemChanged(previousPosition)
                        }

                        item.checkBoxValue = true
                        checkBox.isSelected = item.checkBoxValue
                        selectedPosition = adapterPosition
                        notifyItemChanged(selectedPosition)

                        selectedItem?.invoke(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortByViewHolder {
        val inf = FilterPageItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SortByViewHolder(inf)
    }

    override fun onBindViewHolder(holder: SortByViewHolder, position: Int) {
        holder.bind(modelList[position])
    }

    override fun getItemCount(): Int {
        return modelList.size
    }
}