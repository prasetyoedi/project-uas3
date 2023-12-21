package com.example.project_uas3.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas3.Akun
import com.example.project_uas3.activity.NavigationActivity
import com.example.project_uas3.database.model.TravelData
import com.example.project_uas3.recyclerview.TravelUserAdapter
import com.example.project_uas3.activity.OrderActivity
import com.example.project_uas3.database.model.Favorite
import com.example.project_uas3.database.room.FavoriteDao
import com.example.project_uas3.database.room.roomDb
import com.example.project_uas3.databinding.FragmentHomeUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeUserFragment : Fragment() {

    private lateinit var binding: FragmentHomeUserBinding
    private lateinit var itemAdapter: TravelUserAdapter
    private lateinit var itemList: ArrayList<TravelData>
    private lateinit var recyclerViewItem: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var favoriteDao: FavoriteDao
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = (requireActivity() as NavigationActivity).getDatabase()
        val executor = Executors.newSingleThreadExecutor()
        favoriteDao = db.favoriteDao()!!
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        recyclerViewItem = binding.rvTravel
        recyclerViewItem.setHasFixedSize(true)
        recyclerViewItem.layoutManager = GridLayoutManager(requireContext(), 1)
        itemList = arrayListOf()
        itemAdapter = TravelUserAdapter(itemList) {travelData ->

            val userId = firebaseAuth.currentUser?.uid
            userId?.let { uid ->
                firestore.collection("akun")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { document ->
                        val account = document.toObject(Akun::class.java)
                        account?.let {akun ->
                            executor.execute{
                                favoriteDao.insert(
                                    Favorite(
                                        title = travelData.title!!,
                                        user_email = akun.email,
                                        start = travelData.start!!,
                                        end = travelData.end!!,
                                        price = travelData.price!!,
                                    )
                                )
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error loading user data: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("ProfileFragment", "Error loading user data", e)
                    }
            }
        }
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


    private fun loadUserData() {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let { uid ->
            firestore.collection("akun")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    val account = document.toObject(Akun::class.java)
                    account?.let {

                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error loading user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileFragment", "Error loading user data", e)
                }
        }
    }

}
