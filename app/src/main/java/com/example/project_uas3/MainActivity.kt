package com.example.project_uas3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_uas3.databinding.ActivityMainBinding
import com.example.project_uas3.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

    }
}