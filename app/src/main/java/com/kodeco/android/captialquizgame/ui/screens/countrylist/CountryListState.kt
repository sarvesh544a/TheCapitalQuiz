package com.kodeco.android.captialquizgame.ui.screens.countrylist

import com.kodeco.android.captialquizgame.models.Country

sealed class CountryListState {
    data object Loading : CountryListState()
    data class Success(val countries: List<Country>) : CountryListState()
    data class Error(val error: Throwable) : CountryListState()
}
