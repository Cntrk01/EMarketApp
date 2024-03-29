package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.BasketItemRowBinding
import com.example.e_marketapp.model.MarketBasketEntity
import com.example.e_marketapp.util.clickWithDebounce

class BasketAdapter(
    private val plusClick: ((MarketBasketEntity) -> Unit)? = null,
    private val minusClick: ((MarketBasketEntity) -> Unit)? = null,
) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {

    private var favoriteList = emptyList<MarketBasketEntity>()
    private var totalPriceListener: TotalPriceListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setBasketData(list: List<MarketBasketEntity>) {
        this.favoriteList= emptyList()
        this.favoriteList = list
        notifyDataSetChanged()
    }

    inner class BasketViewHolder(val binding: BasketItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.minusButton.clickWithDebounce {
                handleMinusButtonClick()
            }
            binding.plusButton.clickWithDebounce {
                handlePlusButtonClick()
            }
        }

        private fun handleMinusButtonClick() {
            val itemPosition = favoriteList[adapterPosition]
            if (itemPosition.productCount > 0) {
                itemPosition.productCount -= 1
                binding.itemCount.text = itemPosition.productCount.toString()
                minusClick?.invoke(itemPosition)
            } else {
                Toast.makeText(binding.root.context,itemView.context.getString(R.string.please_add_item), Toast.LENGTH_SHORT).show()
            }
        }

        private fun handlePlusButtonClick() {
            val itemPosition = favoriteList[adapterPosition]
            itemPosition.productCount += 1
            binding.itemCount.text = itemPosition.productCount.toString()
            plusClick?.invoke(itemPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val inf = BasketItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BasketViewHolder(inf)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        var totalPrice=0
        val itemPosition = favoriteList[position]

        for (item in favoriteList) {
            totalPrice += item.singleItemPrice * item.productCount
        }

        totalPriceListener?.onTotalPriceUpdated(totalPrice = totalPrice)

        holder.binding.apply {
            itemName.text = itemPosition.productName
            itemPrice.text = itemPosition.singleItemPrice.toString()
            itemCount.text = itemPosition.productCount.toString()
        }
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Int)
    }

    fun setTotalPriceListener(listener: TotalPriceListener) {
        totalPriceListener = listener
    }
}