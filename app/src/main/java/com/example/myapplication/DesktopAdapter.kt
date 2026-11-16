package com.example.myapplication

import android.R.attr.onClick
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.DesktopItem

class DesktopAdapter(
    private var stockList: List<DesktopItem>,
    private val onClick: (DesktopItem) -> Unit, // <--- ¡Faltaba el "-> Unit" acá!
    private val onLongClick: (DesktopItem) -> Unit
) : RecyclerView.Adapter<DesktopAdapter.DesktopViewHolder>() {

    class DesktopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvStockQuantity: TextView = itemView.findViewById(R.id.tvStockQuantity)
        val tvMaterial: TextView = itemView.findViewById(R.id.tvMaterial)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesktopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_desktop, parent, false)
        return DesktopViewHolder(view)
    }

    override fun onBindViewHolder(holder: DesktopViewHolder, position: Int) {
        val item = stockList[position]
        holder.tvItemName.text = item.name
        holder.tvStockQuantity.text = "Stock: ${item.stock}"
        holder.tvMaterial.text = "Material: ${item.material}"
        holder.tvPrice.text = "Precio: \$${item.price}"

        holder.itemView.setOnClickListener {
            onClick(item)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = stockList.size

    fun updateStock(newList: List<DesktopItem>) {
        stockList = newList
        notifyDataSetChanged()
    }
}