package com.kodeco.android.capitalquizgame.ui.screens.countrylist

import com.kodeco.android.capitalquizgame.models.Country
import com.kodeco.android.capitalquizgame.repositories.CountryRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CountryListViewModelTest {

    private lateinit var viewModel: CountryListViewModel
    private lateinit var mockRepository: CountryRepository
    private val dispatcher = TestCoroutineDispatcher()

    val country = Country(
        commonName = "France",
        mainCapital = "Paris",
        population = 67000000,
        area = 640679f,
        flagUrl = "https://example.com/flag_france.png",
        needsReview = false
    )

    val countryList = listOf(country)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        mockRepository = mockk(relaxed = true)
        viewModel = CountryListViewModel(repository = mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun FetchCountriesSuccessState() = dispatcher.runBlockingTest {
        coEvery { mockRepository.fetchCountries() } just Runs
        coEvery { mockRepository.countries } returns flowOf(countryList)
        viewModel.fetchCountries()
        val states = mutableListOf<CountryListState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }
        advanceUntilIdle()
        assert(states.contains(CountryListState.Success(countryList)))
        job.cancel()
    }

    @Test
    fun FetchCountriesLoadingState() = dispatcher.runBlockingTest {
        coEvery { mockRepository.fetchCountries() } coAnswers {
            delay(500)  // Artificial delay
            flowOf(countryList)
        }
        coEvery { mockRepository.countries } returns flowOf(countryList)

        val states = mutableListOf<CountryListState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }

        viewModel.fetchCountries()

        assertEquals(CountryListState.Loading, viewModel.uiState.value)

        testScheduler.apply { advanceTimeBy(500); runCurrent() }
        assert(states.contains(CountryListState.Success(countryList)))

        job.cancel()
    }

    @Test
    fun ErrorDuringFetchCountries() = dispatcher.runBlockingTest {
        val exception = Exception("Network error")
        coEvery { mockRepository.fetchCountries() } throws exception

        viewModel.fetchCountries()

        advanceUntilIdle()
        assertEquals(CountryListState.Error(exception), viewModel.uiState.value)
    }
    @Test
    fun ToggleNeedsReview() = dispatcher.runBlockingTest {
        coEvery { mockRepository.toggleNeedsReview(any()) } just Runs
        coEvery { mockRepository.countries } returns flowOf(listOf(country.copy(needsReview = true)))
        viewModel.toggleNeedsReview(country)
        advanceUntilIdle()
        assert(viewModel.uiState.value is CountryListState.Success)
        assert((viewModel.uiState.value as CountryListState.Success).countries.first().needsReview)
    }

    @Test
    fun SearchCountries() = dispatcher.runBlockingTest {
        coEvery { mockRepository.searchCountries(any()) } just Runs
        coEvery { mockRepository.countries } returns flowOf(countryList)

        viewModel.searchCountries("France")

        val states = mutableListOf<CountryListState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }
        advanceUntilIdle()
        assert(states.contains(CountryListState.Success(countryList)))
        job.cancel()
    }

    @Test
    fun ErrorDuringSearchCountries() = dispatcher.runBlockingTest {
        val exception = Exception("Search error")
        coEvery { mockRepository.searchCountries(any()) } throws exception
        viewModel.searchCountries("query")
        advanceUntilIdle()
        assertEquals(CountryListState.Error(exception), viewModel.uiState.value)
    }

}