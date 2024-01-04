package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.FavoriteItemRowBinding
import com.example.e_marketapp.local.MarketEntity
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.urlToImageGlide

@SuppressLint("NotifyDataSetChanged")
class FavoriteAdapter(
    private val addToCardClick: (() -> Unit)? = null,
    private val isStarred: ((itemId: String) -> Unit)? = null,
    private val clickItem : ((BaseModelItem)->Unit) ?=null
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var favoriteList = emptyList<MarketEntity>()

    fun setFavoriteList(list: List<MarketEntity>) {
        this.favoriteList = list
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(val binding: FavoriteItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(marketEntity: MarketEntity) {
            binding.apply {
                itemImage.urlToImageGlide(marketEntity.image)
                itemPrice.text = "Price : ${marketEntity.price}"
                itemName.text = "Name : ${marketEntity.name}"

                itemView.setOnClickListener {
                    clickItem?.invoke(BaseModelItem(
                        brand = marketEntity.brand,
                        createdAt=marketEntity.createdAt,
                        description=marketEntity.description,
                        id=marketEntity.marketId,
                        image=marketEntity.image,
                        model=marketEntity.model,
                        name=marketEntity.name,
                        price = marketEntity.price
                    ))
                }

                marketItemStar.setOnClickListener {
                    isStarred?.invoke(marketEntity.marketId)
                    marketItemStar.visibility = View.INVISIBLE
                    marketItemUnStar.visibility = View.VISIBLE
                }
                marketItemUnStar.setOnClickListener {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inf = FavoriteItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(inf)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

}