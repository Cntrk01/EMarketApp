package com.example.e_marketapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_marketapp.databinding.FavoriteItemRowBinding
import com.example.e_marketapp.model.MarketEntity
import com.example.e_marketapp.model.BaseModelItem
import com.example.e_marketapp.util.urlToImageGlide

@SuppressLint("NotifyDataSetChanged")
class FavoriteAdapter(
    private val addToCardClick: ((BaseModelItem) -> Unit)? = null,
    private val isStarred: ((itemId: String) -> Unit)? = null,
    private val clickItem: ((BaseModelItem) -> Unit)? = null
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var favoriteList = emptyList<MarketEntity>()

    fun setFavoriteList(list: List<MarketEntity>) {
        this.favoriteList = list
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(private val binding: FavoriteItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                clickItem?.invoke(getItem(adapterPosition))
            }

            binding.marketItemStar.setOnClickListener {
                isStarred?.invoke(getItem(adapterPosition).id)
            }

            binding.itemAddCard.setOnClickListener {
                addToCardClick?.invoke(getItem(adapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(marketEntity: MarketEntity) {
            binding.apply {
                itemImage.urlToImageGlide(marketEntity.image)
                itemPrice.text = "Price : ${marketEntity.price}"
                itemName.text = "Name : ${marketEntity.name}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(marketEntity = favoriteList[position])
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    private fun getItem(position: Int): BaseModelItem {
        return BaseModelItem(
            brand = favoriteList[position].brand,
            createdAt = favoriteList[position].createdAt,
            description = favoriteList[position].description,
            id = favoriteList[position].marketId,
            image = favoriteList[position].image,
            model = favoriteList[position].model,
            name = favoriteList[position].name,
            price = favoriteList[position].price)
    }
}