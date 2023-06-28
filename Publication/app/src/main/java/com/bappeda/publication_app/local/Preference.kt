package com.bappeda.publication_app.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Preference(private val dataStorePref: DataStore<Preferences>) {
    private val Key = booleanPreferencesKey("theme_setting")

    fun getTheme(): Flow<Boolean> {
        return dataStorePref.data.map { preferences ->
            preferences[Key] ?: false
        }
    }

    suspend fun saveTheme(DarkMode_Active: Boolean) {
        dataStorePref.edit { preferences ->
            preferences[Key] = DarkMode_Active
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Preference? = null

        fun getInstance(data:  DataStore<Preferences>): Preference {
            return INSTANCE ?: synchronized(this) {
                val instance = Preference(data)
                INSTANCE = instance
                instance
            }
        }
    }

}