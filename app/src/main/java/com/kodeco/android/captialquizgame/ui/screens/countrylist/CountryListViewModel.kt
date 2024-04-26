package com.kodeco.android.captialquizgame.ui.screens.countrylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kodeco.android.captialquizgame.models.Country
import com.kodeco.android.captialquizgame.repositories.CountryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val repository: CountryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CountryListState>(CountryListState.Loading)
    val uiState: StateFlow<CountryListState> = _uiState.asStateFlow()

    private var currentSearchJob: Job? = null
    private var fetchCountryJob: Job? = null

    init {
        fetchCountries()
    }

    class CountryInfoViewModelFactory(private val repository: CountryRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CountryListViewModel(repository) as T
    }

    suspend fun updateUIState() {
        val countries = repository.countries.first()
        _uiState.value = CountryListState.Success(countries)
    }

    fun fetchCountries() {
        _uiState.value = CountryListState.Loading
        fetchCountryJob?.cancel()  // Cancel previous search if ongoing

        fetchCountryJob = viewModelScope.launch {
            try {
                repository.fetchCountries()
                updateUIState()
            } catch (e: Exception) {
                _uiState.value = CountryListState.Error(e)
            }
        }
    }

    fun toggleNeedsReview(country: Country) {
        viewModelScope.launch {
            repository.toggleNeedsReview(country)
            updateUIState()
        }
    }

    fun searchCountries(query: String) {
        _uiState.value = CountryListState.Loading
        currentSearchJob?.cancel()  // Cancel previous search if ongoing

        currentSearchJob = viewModelScope.launch {
            try {
                repository.searchCountries(query)
                updateUIState()
            } catch (e: Exception) {
                _uiState.value = CountryListState.Error(e)
            }
        }
    }
}
