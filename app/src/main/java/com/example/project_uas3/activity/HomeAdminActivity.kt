package com.example.project_uas3.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_uas3.R
import com.example.project_uas3.adapter.TravelAdapter
import com.example.project_uas3.database.model.TravelData
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
    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                // Handle logout
                logoutAdmin()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    private fun logoutAdmin() {
        // Clear user session, update SharedPreferences, etc.
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Show a toast message
        Toast.makeText(this@HomeAdminActivity, "Logout successful", Toast.LENGTH_SHORT).show()

        // Redirect to the login screen or perform other actions as needed
        // For example, you can use the following code:
        val intent = Intent(this, LoginRegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

}
