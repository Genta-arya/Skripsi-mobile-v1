package com.example.githubuser.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FvUser::class],
    version = 1
)
abstract class DBFav: RoomDatabase()
{
    companion object {
        var INSTANCE: DBFav? = null


        fun getDatabase(context: Context): DBFav? {
            if (INSTANCE == null) {
                synchronized(DBFav::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DBFav :: class.java, "user_database")
                        .build()
                }
            }
            return INSTANCE
        }
    }
    abstract fun favoriteUserDao(): DaoUserFav
}