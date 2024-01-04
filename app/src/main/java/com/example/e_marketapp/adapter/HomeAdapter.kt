package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.R
import com.example.e_marketapp.databinding.HomeItemRowBinding
import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.clickWithDebounce
import com.example.e_marketapp.util.urlToImageGlide

@SuppressLint("NotifyDataSetChanged")
class HomeAdapter(
    private val clickItemData: ((BaseModelItem) -> Unit)? = null,
    private val clickFavorite: ((BaseModelItem, isStarred: Boolean) -> Unit)? = null
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var marketModelList = emptyList<BaseModelItem>()
    private var marketCheckList = ArrayList<MarketEntity>()

    fun addData(newData: List<BaseModelItem>) {
        this.marketModelList = emptyList()
        this.marketModelList = newData
        notifyDataSetChanged()
        println("ADAPTER LİST SİZE : ${marketModelList.size}")
    }

    fun checkList(checkList: ArrayList<MarketEntity>) {
        this.marketCheckList = checkList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: HomeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(marketItem: BaseModelItem) {
            binding.apply {
                marketItemImage.urlToImageGlide(marketItem.image)
                marketItemPrice.text = marketItem.price + "$"
                marketItemName.text = marketItem.name

                itemView.setOnClickListener {
                    clickItemData?.invoke(marketItem)
                }

                marketItemStar.setOnClickListener {
                    clickFavorite?.invoke(marketItem, false)

                    val removedItem = marketCheckList.find { it.marketId == marketItem.id }
                    removedItem?.let {
                        marketCheckList.remove(it)

                    }
                    binding.marketItemUnStar.visibility = View.VISIBLE
                    binding.marketItemStar.visibility = View.INVISIBLE
                }
                marketItemUnStar.setOnClickListener {
                    val removedItem = marketCheckList.find { it.marketId == marketItem.id }
                    removedItem?.let {
                        marketCheckList.add(it)

                    }
                    clickFavorite?.invoke(marketItem, true)
                    binding.marketItemStar.visibility = View.VISIBLE
                    binding.marketItemUnStar.visibility = View.INVISIBLE
                }

                if (isStarred(marketItem.id)) {
                    binding.marketItemStar.visibility = View.VISIBLE
                    binding.marketItemUnStar.visibility = View.INVISIBLE
                } else {
                    binding.marketItemUnStar.visibility = View.VISIBLE
                    binding.marketItemStar.visibility = View.INVISIBLE
                }

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

    private fun isStarred(id: String): Boolean {
        return marketCheckList.any { it.marketId == id }
    }
}