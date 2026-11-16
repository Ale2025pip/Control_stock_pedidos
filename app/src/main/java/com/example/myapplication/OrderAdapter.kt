package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.OrderModel

class OrderAdapter(
    private var ordersList: List<OrderModel>,
    private val onLongClick: (OrderModel) -> Unit // <--- Añadimos el callback para el toque largo
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvClientName: TextView = itemView.findViewById(R.id.tvClientName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvTotalPrice: TextView = itemView.findViewById(R.id.tvTotalPrice)
        val tvAdvancePayment: TextView = itemView.findViewById(R.id.tvAdvancePayment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.tvClientName.text = order.clientName
        holder.tvStatus.text = order.status
        holder.tvDescription.text = order.description
        holder.tvTotalPrice.text = "Total: \$${order.totalPrice}"
        holder.tvAdvancePayment.text = "Seña: \$${order.advancePayment}"

        // <--- Detectamos el toque largo en la tarjeta para activar la eliminación
        holder.itemView.setOnLongClickListener {
            onLongClick(order)
            true // Retornamos true para indicar que el evento de clic largo fue consumido
        }
    }

    override fun getItemCount(): Int = ordersList.size

    // Método para actualizar los datos en tiempo real cuando cambie el Flow
    fun updateOrders(newOrders: List<OrderModel>) {
        ordersList = newOrders
        notifyDataSetChanged()
    }
}