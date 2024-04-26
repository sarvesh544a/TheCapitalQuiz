package com.kodeco.android.captialquizgame.repositories

import com.kodeco.android.captialquizgame.models.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    val countries: Flow<List<Country>>
    suspend fun fetchCountries()
    fun getCountry(index: Int): Country?

    suspend fun getRandonCountries(count: Int): List<Country>

    suspend fun getRandomCapitals(correctCapital: String, limit: Int): List<String>

    suspend fun toggleNeedsReview(country: Country)

    suspend fun setNeedsReview(country: Country)

    suspend fun searchCountries(query: String)

}
