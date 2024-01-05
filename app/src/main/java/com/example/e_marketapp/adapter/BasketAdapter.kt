package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.BasketItemRowBinding
import com.example.e_marketapp.local.MarketBasketEntity
import com.example.e_marketapp.util.clickWithDebounce

class BasketAdapter(
    private val plusClick : ((MarketBasketEntity)->Unit)?=null,
    private val minusClick : ((MarketBasketEntity)->Unit)?=null
) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>(){

    private var favoriteList = emptyList<MarketBasketEntity>()

    @SuppressLint("NotifyDataSetChanged")
    fun setBasketData(list:List<MarketBasketEntity>){
        this.favoriteList=list
        notifyDataSetChanged()
    }

    inner class BasketViewHolder(val binding: BasketItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val inf=BasketItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BasketViewHolder(inf)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        var totalPrice=0.0
        val itemPosition=favoriteList[position]
        for (item in favoriteList) {
            totalPrice += item.productPrice
        }

        totalPriceListener?.onTotalPriceUpdated(totalPrice = totalPrice)

        holder.binding.apply {
            itemName.text = itemPosition.productName
            itemPrice.text = itemPosition.productPrice.toString()
            itemCount.text = itemPosition.productCount.toString()

            minusButton.clickWithDebounce {
                if (itemPosition.productCount > 0) {
                    itemPosition.productCount -= 1
                    itemCount.text = itemPosition.productCount.toString()
                    minusClick?.invoke(itemPosition)
                } else {
                    minusButton.isClickable = false
                    Toast.makeText(holder.itemView.context, "Please Add Item", Toast.LENGTH_SHORT).show()
                }
            }
            plusButton.clickWithDebounce {
                itemPosition.productCount +=1
                itemCount.text = (itemPosition.productCount).toString()
                plusClick?.invoke(itemPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    private var totalPriceListener: TotalPriceListener? = null

    interface TotalPriceListener {
        fun onTotalPriceUpdated(totalPrice: Double)
    }
    fun setTotalPriceListener(listener: TotalPriceListener) {
        totalPriceListener = listener
    }
}