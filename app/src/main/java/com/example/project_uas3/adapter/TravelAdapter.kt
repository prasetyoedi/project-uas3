package com.example.project_uas3.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.project_uas3.R
import com.example.project_uas3.database.model.TravelData
import com.example.project_uas3.activity.EditAdminActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class TravelAdapter(private val travelAdminList: ArrayList<TravelData>) : RecyclerView.Adapter<TravelAdapter.TravelAdminViewHolder>() {

    class TravelAdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_travel)
        val start: TextView = itemView.findViewById(R.id.start_travel)
        val end: TextView = itemView.findViewById(R.id.end_travel)
        val price: TextView = itemView.findViewById(R.id.price_travel)
        val description: TextView = itemView.findViewById(R.id.description_travel)
        val image: ImageView = itemView.findViewById(R.id.image_travel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelAdminViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admintravel, parent, false)
        return TravelAdminViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TravelAdminViewHolder, position: Int) {
        val currentItem = travelAdminList[position]

        holder.title.text = currentItem.title
        holder.start.text = currentItem.start
        holder.end.text = currentItem.end
        holder.price.text = currentItem.price
        holder.description.text = currentItem.description

        // Use Glide or Picasso to load the image from the URL into the ImageView
        Glide.with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .skipMemoryCache(true) // Skip caching in memory
            .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
            .into(holder.image)

        holder.itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener{
            val intent = Intent(holder.itemView.context, EditAdminActivity::class.java)
            val currentItem = travelAdminList[position]
            intent.putExtra("title", currentItem.title)
            intent.putExtra("start", currentItem.start)
            intent.putExtra("end", currentItem.end)
            intent.putExtra("price", currentItem.price)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("imgId", currentItem.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.findViewById<ImageButton>(R.id.btn_hapus).setOnClickListener{
            val itemToDelete = Uri.parse(currentItem.imageUrl).lastPathSegment?.removePrefix("images/")

            // Remove the item from the list
            travelAdminList.removeAt(position)

            // Notify the adapter of the data change
            notifyDataSetChanged()

            // Delete the corresponding data from the Realtime Database
            deleteItemFromDatabase(itemToDelete.toString())
        }

    }

    private fun deleteItemFromDatabase(imgId: String) {
        // Reference to the Firebase Storage
        val storageReference = FirebaseStorage.getInstance().getReference("images").child(imgId)

        // Delete the image from Firebase Storage
        storageReference.delete().addOnSuccessListener {
            // Image deleted successfully, now delete the corresponding data from the Realtime Database
            val database = FirebaseDatabase.getInstance().getReference("Travel")
            database.child(imgId).removeValue()
                .addOnCompleteListener {
                    // Handle success if needed
                }
                .addOnFailureListener {
                    // Handle failure if needed
                }
        }.addOnFailureListener {
            // Handle failure if the image deletion fails
        }
    }

    override fun getItemCount() = travelAdminList.size
}
