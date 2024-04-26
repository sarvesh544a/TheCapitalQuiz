package com.kodeco.android.worldcountriesquiz.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val STORE_NAME = "worldcountries_settings"

class SettingsPrefsImpl @Inject constructor(@ApplicationContext context: Context) : SettingsPrefs
{
    private val Context.dataStore by preferencesDataStore(name = STORE_NAME)

    private val dataStore = context.dataStore
    override fun getLocalStorageEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SettingsPreferenceKeys.ENABLE_DATA_STORAGE] ?: true
        }
    }

    override fun getReviewCountryEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[SettingsPreferenceKeys.ENABLE_REVIEWCOUNTRY] ?: true
        }
    }

    override suspend fun toggleLocalStorage() {
        dataStore.edit { preferences ->
            val current = preferences[SettingsPreferenceKeys.ENABLE_DATA_STORAGE] ?: true
            preferences[SettingsPreferenceKeys.ENABLE_DATA_STORAGE] = !current
        }
    }

    override suspend fun toggleReviewCountryFeature() {
        dataStore.edit { preferences ->
            val current = preferences[SettingsPreferenceKeys.ENABLE_REVIEWCOUNTRY] ?: true
            preferences[SettingsPreferenceKeys.ENABLE_REVIEWCOUNTRY] = !current
        }
    }

}