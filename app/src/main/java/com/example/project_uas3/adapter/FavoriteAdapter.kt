package com.example.project_uas3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas3.R
import com.example.project_uas3.database.model.Favorite

class FavoriteAdapter(private val favoriteAdminList: List<Favorite>, private val onDelete: (Favorite) -> Unit) : RecyclerView.Adapter<FavoriteAdapter.FavoriteAdminViewHolder>() {

    class FavoriteAdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_travel)
        val start: TextView = itemView.findViewById(R.id.start_travel)
        val end: TextView = itemView.findViewById(R.id.end_travel)
        val price: TextView = itemView.findViewById(R.id.price_travel)
        val image: ImageView = itemView.findViewById(R.id.image_travel)
        val btnHapus: ImageButton = itemView.findViewById(R.id.btn_hapus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdminViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admintravel, parent, false)
        return FavoriteAdminViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteAdminViewHolder, position: Int) {
        val currentItem = favoriteAdminList[position]
        holder.title.text = currentItem.title
        holder.start.text = currentItem.start
        holder.end.text = currentItem.end
        holder.price.text = currentItem.price
        holder.btnHapus.setOnClickListener {
            onDelete(currentItem)
        }
//        // Use Glide or Picasso to load the image from the URL into the ImageView
//        Glide.with(holder.itemView.context)
//            .load(currentItem)
//            .centerCrop()
//            .skipMemoryCache(true)
//            .diskCacheStrategy(DiskCacheStrategy.NONE)
//            .into(holder.image)

//        holder.image.load(R.drawable.destination_1)

//        holder.image.load("https://raw.githubusercontent.com/prasetyoedi/polines/master/assets/img/acara/1.png")
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount() = favoriteAdminList.size
}
