package com.progdist.egm.proyectopdist.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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

    val idSelectedBranch: Flow<Int?>
        get() = dataStore.data.map { preferences ->
            preferences[WORK_BRANCH]
        }

    suspend fun saveIdWorkingBranch(branchId: Int){
        dataStore.edit { preferences ->
            preferences[WORK_BRANCH] = branchId
        }
    }

    suspend fun deleteIdWorkingBranch(){
        dataStore.edit { preferences ->
            preferences.remove(WORK_BRANCH)
        }
    }

    suspend fun saveAuthToken(authToken: String){
        dataStore.edit { preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    suspend fun deleteAuthToken(){
        dataStore.edit { preferences ->
            preferences.remove(WORK_BRANCH)
            preferences.remove(KEY_AUTH)
        }
    }

    companion object {
        private val KEY_AUTH = stringPreferencesKey("key_auth")
        private val WORK_BRANCH = intPreferencesKey("work_branch_id")
    }

}