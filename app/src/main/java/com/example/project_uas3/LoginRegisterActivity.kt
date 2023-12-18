package com.example.project_uas3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_uas3.databinding.ActivityLoginRegisterBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Check login status
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val adapter = LoginRegisterAdapter(this)
            binding.viewPager.adapter = adapter
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> "Invalid"
                }
            }.attach()
        }
    }
}
