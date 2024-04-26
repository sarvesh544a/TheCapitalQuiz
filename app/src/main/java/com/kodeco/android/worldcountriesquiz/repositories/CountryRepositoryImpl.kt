package com.kodeco.android.worldcountriesquiz.repositories

import android.util.Log
import com.kodeco.android.worldcountriesquiz.database.CountryDao
import com.kodeco.android.worldcountriesquiz.database.SettingsPrefs
import com.kodeco.android.worldcountriesquiz.models.Country
import com.kodeco.android.worldcountriesquiz.network.CountryService
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

class CountryRepositoryImpl(
    private val service: CountryService,
    private val countryDao: CountryDao,
    private val prefs: SettingsPrefs,
    ) : CountryRepository {

    private val _countries: MutableStateFlow<List<Country>> = MutableStateFlow(emptyList())
    override val countries: StateFlow<List<Country>> = _countries.asStateFlow()

    private suspend fun getLocalStorageToggleStatus(): Boolean?  {
        return coroutineScope {
            prefs.getLocalStorageEnabled().firstOrNull()
        }
    }

    override suspend fun fetchCountries() {
        try {
            val localStorageToggleStatus = getLocalStorageToggleStatus()

            val needsReviewCountries = if (localStorageToggleStatus == true  || getLocalStorageToggleStatus() == null) {
                countryDao.getNeedsReviewCountries()
            } else {
                listOf()
            }

            _countries.value = emptyList()

            val countriesResponse = service.getAllCountries()
            if (countriesResponse.isSuccessful) {
                val countries = countriesResponse.body() ?: emptyList()
                if (localStorageToggleStatus == true  || getLocalStorageToggleStatus() == null) {
                    val countriesWithNeedsReview = countries.map { country ->
                        country.copy(needsReview = needsReviewCountries.any { it.commonName == country.commonName })
                    }
                    countryDao.deleteAllCountries()
                    countryDao.insertCountries(countriesWithNeedsReview)
                    _countries.value = countriesWithNeedsReview
                } else
                    _countries.value = countries

            } else {
                Log.e(
                    "CountryRepository",
                    "Error fetching countries: ${countriesResponse.message()}"
                )
                if (localStorageToggleStatus == true)
                    _countries.value = countryDao.getAllCountries()
                else throw Exception("Request failed: ${countriesResponse.message()}")
            }
        } catch (e: Exception) {
            Log.e("CountryRepository", "Error fetching countries", e)
            if (getLocalStorageToggleStatus() == true)
                _countries.value = countryDao.getAllCountries()
            else throw Exception("Exception")
        }
    }

    override fun getCountry(index: Int): Country? =
        _countries.value.getOrNull(index)

    override suspend fun getRandonCountries(count: Int): List<Country> {
        return countryDao.getRandomCountries(count)
    }

    override suspend fun getRandomCapitals(correctCapital: String, limit: Int): List<String> {
        return countryDao.getRandomCapitals(correctCapital, limit)
    }


    override suspend fun toggleNeedsReview(country: Country) {
        val index = _countries.value.indexOf(country)
        val mutableCountries = _countries.value.toMutableList()
        val updatedCountry = country.copy(needsReview = !country.needsReview)
        mutableCountries[index] = updatedCountry
        countryDao.updateCountry(updatedCountry)
        _countries.value = mutableCountries.toList()
    }

    override suspend fun setNeedsReview(country: Country) {
        val updatedCountry = country.copy(needsReview = true)
        countryDao.updateCountry(updatedCountry)
    }

    override suspend fun searchCountries(query: String) {
        val searchResult = countryDao.searchCountries(query)
        _countries.value = searchResult
    }

}
