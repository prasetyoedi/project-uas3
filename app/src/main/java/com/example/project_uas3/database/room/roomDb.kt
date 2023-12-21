package com.example.project_uas3.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_uas3.database.model.Favorite

@Database(entities = [Favorite::class ], version = 2, exportSchema = false)
abstract class roomDb : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao?

    companion object {
        @Volatile
        private var INSTANCE: roomDb? = null
        fun getDatabase(context: Context): roomDb? {
            if (INSTANCE == null) {
                synchronized(roomDb::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        roomDb::class.java,
                        "travel_db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }
}