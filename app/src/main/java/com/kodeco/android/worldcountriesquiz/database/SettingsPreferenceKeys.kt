package com.kodeco.android.worldcountriesquiz.database

import androidx.datastore.preferences.core.booleanPreferencesKey

object SettingsPreferenceKeys {
    val ENABLE_REVIEWCOUNTRY = booleanPreferencesKey("enable_review_country")
    val ENABLE_DATA_STORAGE = booleanPreferencesKey("enable_data_storage")
}