package com.example.project_uas3.database.model

data class OrderData(
    val title: String,
    val start: String,
    val end: String,
    val price: String,
    val description: String,
    val date: String,
    var orderId: String,
    var imageUrl: String
) {
    // Add a no-argument constructor for Firebase deserialization
    constructor() : this("", "", "", "", "", "", "", "")
}
