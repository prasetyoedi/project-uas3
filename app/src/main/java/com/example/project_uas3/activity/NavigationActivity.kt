package com.example.project_uas3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.project_uas3.fragment.FavoriteFragment
import com.example.project_uas3.fragment.HomeUserFragment
import com.example.project_uas3.R
import com.example.project_uas3.database.room.roomDb
import com.example.project_uas3.databinding.ActivityNavigationBinding
import com.example.project_uas3.fragment.HistoryOrderFragment
import com.example.project_uas3.fragment.ProfileFragment
import java.util.concurrent.ExecutorService

class NavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var databaseRoom: roomDb


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseRoom = roomDb.getDatabase(this)!!

        replaceFragment(HomeUserFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeUserFragment())
                R.id.nav_favorite -> replaceFragment(FavoriteFragment())
                R.id.nav_history -> replaceFragment(HistoryOrderFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    fun getDatabase(): roomDb {
        return databaseRoom
    }
}
