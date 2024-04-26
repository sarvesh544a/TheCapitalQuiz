package com.kodeco.android.worldcountriesquiz.network

import com.kodeco.android.worldcountriesquiz.models.Country
import com.kodeco.android.worldcountriesquiz.network.adapters.WrappedCountryList
import retrofit2.Response
import retrofit2.http.GET

interface CountryService {
    @GET("v3.1/all")
    @WrappedCountryList
    suspend fun getAllCountries(): Response<List<Country>>
}
