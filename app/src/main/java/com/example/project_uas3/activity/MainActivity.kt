package com.example.project_uas3.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_uas3.R
import com.example.project_uas3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

    }
}