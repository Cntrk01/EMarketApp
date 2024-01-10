package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.HomeItemRowBinding
import com.example.e_marketapp.model.MarketEntity
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.urlToImageGlide

@SuppressLint("NotifyDataSetChanged")
class HomeAdapter(
    private val clickActionFragment: ((BaseModelItem) -> Unit)? = null,
    private val clickFavorite: ((BaseModelItem, isStarred: Boolean) -> Unit)? = null,
    private val addToCardClick: ((BaseModelItem) -> Unit)? = null,
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var marketModelList = emptyList<BaseModelItem>()
    private var marketCheckList = mutableSetOf<MarketEntity>()

    fun addData(newData: List<BaseModelItem>) {
        this.marketModelList = newData
        notifyDataSetChanged()
        println("ADAPTER LİST SİZE : ${marketModelList.size}")
    }

    fun checkList(checkList: List<MarketEntity>) {
        this.marketCheckList = checkList.toMutableSet()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: HomeItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                clickActionFragment?.invoke(marketModelList[adapterPosition])
            }

            binding.addToCardItem.setOnClickListener {
                addToCardClick?.invoke(marketModelList[adapterPosition])
            }

            binding.marketItemStar.setOnClickListener {
                handleStarClick(isStarred(adapterPosition))
            }

            binding.marketItemUnStar.setOnClickListener {
                handleStarClick(!isStarred(adapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(marketItem: BaseModelItem) {
            binding.apply {
                marketItemImage.urlToImageGlide(marketItem.image)
                marketItemPrice.text = marketItem.price.toString() + "$"
                marketItemName.text = marketItem.name

                updateStarVisibility(isStarred(adapterPosition))
            }
        }

        private fun handleStarClick(isStarred: Boolean) {
            val marketItem = marketModelList[adapterPosition]
            clickFavorite?.invoke(marketItem, isStarred)

            if (isStarred) {
                marketCheckList.remove(findMarketEntityById(marketItem.id))
            } else {
                findMarketEntityById(marketItem.id)?.let { marketCheckList.add(it) }
            }
            updateStarVisibility(!isStarred)
        }

        private fun updateStarVisibility(isStarred: Boolean) {
            binding.marketItemStar.visibility = if (isStarred) View.VISIBLE else View.INVISIBLE
            binding.marketItemUnStar.visibility = if (isStarred) View.INVISIBLE else View.VISIBLE
        }

        private fun isStarred(position: Int): Boolean {
            val marketItem = marketModelList[position]
            return marketCheckList.any { it.marketId == marketItem.id }
        }

        private fun findMarketEntityById(id: String): MarketEntity? {
            return marketCheckList.find { it.marketId == id }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inf = HomeItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inf)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(marketModelList[position])
    }

    override fun getItemCount(): Int {
        return marketModelList.size
    }
}
