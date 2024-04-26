package com.kodeco.android.capitalquizgame.ui.screens.countrydetails

import com.kodeco.android.capitalquizgame.models.Country
import com.kodeco.android.capitalquizgame.repositories.CountryRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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

class CountryDetailsViewModelTest {

    private lateinit var viewModel: CountryDetailsViewModel
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

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockRepository = mockk(relaxed = true)
        viewModel = CountryDetailsViewModel(repository = mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCountryDetailsSuccessState() = dispatcher.runBlockingTest {
        coEvery { mockRepository.getCountry(1) } returns country
        val states = mutableListOf<CountryDetailsState>()
        val job = launch {
            viewModel.uiState.toList(states)
        }
        viewModel.getCountryDetails(1)
        advanceUntilIdle()
        assertEquals(CountryDetailsState.Success(country), viewModel.uiState.first())
        job.cancel()
    }
}