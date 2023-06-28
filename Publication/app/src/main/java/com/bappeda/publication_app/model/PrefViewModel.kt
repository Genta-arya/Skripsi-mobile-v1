package com.bappeda.publication_app.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bappeda.publication_app.local.Preference

import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class PrefViewModel(private val pref: Preference) : ViewModel()  {

    fun getTheme(): LiveData<Boolean> {
        return pref.getTheme().asLiveData()
    }

    fun saveThemes(DarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveTheme(DarkModeActive)
        }
    }
}