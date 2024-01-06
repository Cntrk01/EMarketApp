package com.example.e_marketapp.adapter.filter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.FilterPageItemRowBinding
import com.example.e_marketapp.model.FilterModelItem

class BrandAdapter (
    private val selectedItem : ((FilterModelItem)->Unit) ?=null
): RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    private var brandList = emptyList<FilterModelItem>()
    private var selectedPosition = -1

    @SuppressLint("NotifyDataSetChanged")
    fun setBrandList(list : List<FilterModelItem>){
        this.brandList=list
        notifyDataSetChanged()
    }

    inner class BrandViewHolder(val binding : FilterPageItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: FilterModelItem) {
            binding.apply {
                checkBox.isSelected = item.checkBoxValue
                checkText.text = item.title

                checkBox.setOnClickListener {
                    if (!item.checkBoxValue) {
                        val previousPosition = selectedPosition
                        if (previousPosition != RecyclerView.NO_POSITION) {
                            brandList[previousPosition].checkBoxValue = false
                            notifyItemChanged(previousPosition)
                        }

                        item.checkBoxValue = true
                        checkBox.isSelected=item.checkBoxValue
                        selectedPosition = adapterPosition
                        notifyItemChanged(selectedPosition)

                        selectedItem?.invoke(item)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val inf = FilterPageItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BrandViewHolder(inf)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(brandList[position])
    }

    override fun getItemCount(): Int {
        return brandList.size
    }
}