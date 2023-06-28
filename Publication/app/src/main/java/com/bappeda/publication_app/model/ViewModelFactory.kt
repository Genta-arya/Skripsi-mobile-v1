package com.bappeda.publication_app.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bappeda.publication_app.local.Preference
import java.util.prefs.Preferences


class ViewModelFactory(private val pref: Preference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrefViewModel::class.java)) {
            return PrefViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}