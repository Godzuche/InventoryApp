package com.example.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.data.Item
import com.example.inventory.data.getFormattedPrice
import com.example.inventory.databinding.ItemListItemBinding

class ItemListAdapter(private val onItemClicked: (Item) -> Unit) :
    ListAdapter<Item, ItemListAdapter.ItemListViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListViewHolder {
        return ItemListViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.current = currentItem
        holder.bind(currentItem)
    }

    inner class ItemListViewHolder(private var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var current: Item? = null
        init {
            itemView.setOnClickListener {
                current?.let { item -> onItemClicked(item) }
            }
        }

        fun bind(item: Item) {
            binding.apply {
                itemName.text = item.itemName
                itemPrice.text = item.getFormattedPrice()
                itemQuantity.text = item.quantityInStock.toString()
            }
        }
    }

    companion object {
        private val DiffCallback = object: DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.itemName == newItem.itemName
            }
        }
    }

}