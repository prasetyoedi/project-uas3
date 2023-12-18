package com.example.project_uas3

import java.io.Serializable

data class OrderData(
    val title: String,
    val start: String,
    val end: String,
    val price: String,
    val description: String,
    val date: String
) : Serializable
