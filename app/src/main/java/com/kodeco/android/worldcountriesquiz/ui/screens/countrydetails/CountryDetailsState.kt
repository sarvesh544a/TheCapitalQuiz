package com.kodeco.android.worldcountriesquiz.ui.screens.countrydetails

import com.kodeco.android.worldcountriesquiz.models.Country

sealed class CountryDetailsState {
    data object Loading : CountryDetailsState()
    data class Success(val country: Country) : CountryDetailsState()
    data class Error(val error: Throwable) : CountryDetailsState()
}
