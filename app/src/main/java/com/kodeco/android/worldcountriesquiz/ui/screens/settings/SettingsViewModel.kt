package com.kodeco.android.worldcountriesquiz.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kodeco.android.worldcountriesquiz.database.SettingsPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val worldCountriesPrefs: SettingsPrefs) : ViewModel() {
    private val _enableNeedsReview = MutableStateFlow(true)
    val enableNeedsReview = _enableNeedsReview

    private val _enableDataStorage = MutableStateFlow(true)
    val enableDataStorage = _enableDataStorage

    init {
        viewModelScope.launch {
            worldCountriesPrefs.getLocalStorageEnabled().collect {
                _enableDataStorage.value = it
            }
        }

        viewModelScope.launch {
            worldCountriesPrefs.getReviewCountryEnabled().collect {
                _enableNeedsReview.value = it
            }
        }
    }

    class CountryInfoSettingsViewModelFactory(private val worldCountriesPrefs: SettingsPrefs) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SettingsViewModel(worldCountriesPrefs) as T
    }

    fun toggleReviewCountry() {
        viewModelScope.launch {
            worldCountriesPrefs.toggleReviewCountryFeature()
        }
    }

    fun toggleDataStorage() {
        viewModelScope.launch {
            worldCountriesPrefs.toggleLocalStorage()
        }
    }
}