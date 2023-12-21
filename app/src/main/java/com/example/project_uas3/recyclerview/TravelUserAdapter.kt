package com.example.project_uas3.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_uas3.R
import com.example.project_uas3.database.model.TravelData

class TravelUserAdapter(private val filmUserList: ArrayList<TravelData>, private val addFavorite:(TravelData) -> Unit) : RecyclerView.Adapter<TravelUserAdapter.TravelUserViewHolder>() {

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class TravelUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val titleUser: TextView = itemView.findViewById(R.id.title_destination_user)
        val startUser: TextView = itemView.findViewById(R.id.start_station_user)
        val endUser: TextView = itemView.findViewById(R.id.end_station_user)
        val imageUser: ImageView = itemView.findViewById(R.id.image_film_user)
        val btnFavorite: ToggleButton = itemView.findViewById(R.id.btn_fav)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener?.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_travel, parent, false)
        return TravelUserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TravelUserViewHolder, position: Int) {
        val currentItem = filmUserList[position]
        holder.titleUser.text = currentItem.title
        holder.startUser.text = currentItem.start
        holder.endUser.text = currentItem.end
        holder.btnFavorite.setOnClickListener {
            addFavorite(currentItem)
        }
        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .into(holder.imageUser)
    }

    override fun getItemCount() = filmUserList.size
}
