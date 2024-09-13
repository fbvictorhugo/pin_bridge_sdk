package dev.fbvictorhugo.pin_bridge_sdk.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "datastore")

private val TOKEN_KEY = stringPreferencesKey("key_token")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun getToken(): String? {
        return dataStore.data.map { preferences -> preferences[TOKEN_KEY] }.first()
    }

    suspend fun saveToken(token: String?) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = "" + token
        }
    }

}
