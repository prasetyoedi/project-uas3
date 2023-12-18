package com.example.project_uas3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orderList: MutableList<OrderData>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_destination_order)
        val startTextView: TextView = itemView.findViewById(R.id.start_station_order)
        val endTextView: TextView = itemView.findViewById(R.id.end_station_order)
        val priceTextView: TextView = itemView.findViewById(R.id.price_order)
        val dateTextView: TextView = itemView.findViewById(R.id.datepicker_order)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orderview, parent, false)
        return OrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentOrder = orderList[position]

        holder.titleTextView.text = currentOrder.title
        holder.startTextView.text = currentOrder.start
        holder.endTextView.text = currentOrder.end
        holder.priceTextView.text = currentOrder.price
        holder.dateTextView.text = currentOrder.date
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    fun addOrder(order: OrderData) {
        orderList.add(order)
        notifyItemInserted(orderList.size - 1)
    }
}
