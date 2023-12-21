package com.example.project_uas3.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.project_uas3.database.model.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favourite: Favorite)
    @Update
    fun update(favourite: Favorite)
    @Delete
    fun delete(favourite: Favorite)

    @Query("SELECT * FROM favorit WHERE user_email = :userEmail ORDER BY id ASC")
    fun getUserFavourites(userEmail: String): LiveData<List<Favorite>>
    @Query("SELECT * FROM favorit WHERE id_travel = :idTravel AND user_email = :userEmail")
    fun getUserFavListByTravel(idTravel: String, userEmail: String): List<Favorite>
    @Query("DELETE FROM favorit WHERE id_travel = :idTravel AND user_email = :userEmail")
    fun deleteUserFavByTravel(idTravel: String, userEmail: String)
    @Query("SELECT * FROM favorit WHERE id_travel = :idTravel AND user_email = :userEmail LIMIT 1")
    fun getUserFavByTravel(idTravel: String, userEmail: String): Favorite
}