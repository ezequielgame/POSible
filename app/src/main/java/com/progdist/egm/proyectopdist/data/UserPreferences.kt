package com.progdist.egm.proyectopdist.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferences (
    context: Context
) {

    private val applicationContext = context.applicationContext
    private val dataStore = context.dataStore

    val authToken: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_AUTH]
        }


    suspend fun saveAuthToken(authToken: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }

}