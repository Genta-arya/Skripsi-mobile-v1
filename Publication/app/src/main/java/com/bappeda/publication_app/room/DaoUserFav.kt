package com.example.githubuser.Room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoUserFav {
    @Insert
    fun addFavorite(favoriteUser: FvUser)

    @Query("SELECT * from favorite_user")
    fun getUser(): LiveData<List<FvUser>>

    @Query("DELETE from favorite_user WHERE favorite_user.id = :id")
    fun DeleteData(id: Int):Int
//
    @Query("DELETE from favorite_user ")
    fun DeleteDataAll()

    @Query("SELECT count(*) from favorite_user WHERE favorite_user.id = :id")
    fun cekUserId(id:Int): Int

    @Delete
    fun delete(favoriteUser: FvUser)



}