package com.example.project_uas3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas3.databinding.ActivityHomeAdminBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeAdminBinding
    private lateinit var itemAdapter: TravelAdapter
    private lateinit var itemList: ArrayList<TravelData>
    private lateinit var recyclerViewItem: RecyclerView

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerViewItem = binding.rvTravel
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()
        itemAdapter = TravelAdapter(itemList)
        recyclerViewItem.adapter = itemAdapter

        binding.btnPlusAdmin.setOnClickListener {
            startActivity(Intent(this, AddAdminActivity::class.java))
        }

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
                // Handle onCancelled if needed
            }
        })
    }
}
