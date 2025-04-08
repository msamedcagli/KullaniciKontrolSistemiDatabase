package com.msamedcagli.delsaappson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msamedcagli.delsaappson.databinding.ItemViewBinding

class RecyclerViewAdapter(
    private val itemList: ArrayList<Item>,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.binding.itemName.text = currentItem.name
        holder.binding.itemImage.setImageResource(currentItem.imageResId)
        holder.itemView.setOnClickListener { onItemClick(currentItem) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root)
}
