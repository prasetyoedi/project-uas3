package com.example.project_uas3.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.auth.User

@Entity(tableName = "favorit")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "id_travel")
    val id_travel: String = "",
    @ColumnInfo(name = "user_email")
    val user_email: String = "",
    @ColumnInfo(name = "title")
    var title: String = "",
    @ColumnInfo(name = "start")
    val start: String = "",
    @ColumnInfo(name = "end")
    var end: String = "",
    @ColumnInfo(name = "price")
    var price: String = ""
)
