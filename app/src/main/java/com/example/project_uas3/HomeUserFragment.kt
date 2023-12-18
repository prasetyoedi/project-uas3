package com.example.project_uas3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas3.databinding.FragmentHomeUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeUserFragment : Fragment() {

    private lateinit var binding: FragmentHomeUserBinding
    private lateinit var itemAdapter: TravelUserAdapter
    private lateinit var itemList: ArrayList<TravelData>
    private lateinit var recyclerViewItem: RecyclerView

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewItem = binding.rvTravel
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = GridLayoutManager(requireContext(), 1)

        itemList = arrayListOf()
        itemAdapter = TravelUserAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        // Set item click listener
        itemAdapter.setOnItemClickListener(object : TravelUserAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                // Handle item click here
                val clickedItem: TravelData = itemList[position]

                // Create an intent to start OrderActivity
                val intent = Intent(requireContext(), OrderActivity::class.java)

                // Put the necessary data into the intent
                intent.putExtra("title", clickedItem.title)
                intent.putExtra("start", clickedItem.start)
                intent.putExtra("end", clickedItem.end)
                intent.putExtra("price", clickedItem.price)
                intent.putExtra("description", clickedItem.description)
                intent.putExtra("imageUrl", clickedItem.imageUrl)

                // Start OrderActivity with the intent
                startActivity(intent)
            }
        })

        // Fetch data from Firebase
        database = FirebaseDatabase.getInstance().getReference("Travel")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(TravelData::class.java)
                    if (item != null) {
                        itemList.add(item)
                    }
                }
                itemAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if needed
            }
        })
    }
}
