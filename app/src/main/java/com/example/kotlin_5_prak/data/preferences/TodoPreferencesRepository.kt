package com.example.kotlin_5_prak.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//создаёт DataStore один раз
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "todo_prefs")

class TodoPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        val HIGHLIGHT_DONE = booleanPreferencesKey("highlight_done")
    }

    val highlightDone: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[HIGHLIGHT_DONE] ?: false
    }

    suspend fun setHighlightDone(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[HIGHLIGHT_DONE] = value
        }
    }
}