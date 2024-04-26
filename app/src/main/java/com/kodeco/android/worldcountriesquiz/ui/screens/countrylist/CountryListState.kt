package com.kodeco.android.worldcountriesquiz.ui.screens.countrylist

import com.kodeco.android.worldcountriesquiz.models.Country

sealed class CountryListState {
    data object Loading : CountryListState()
    data class Success(val countries: List<Country>) : CountryListState()
    data class Error(val error: Throwable) : CountryListState()
}
