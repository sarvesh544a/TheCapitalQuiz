package com.kodeco.android.worldcountriesquiz.repositories

import android.util.Log
import com.kodeco.android.worldcountriesquiz.database.CountryDao
import com.kodeco.android.worldcountriesquiz.database.SettingsPrefs
import com.kodeco.android.worldcountriesquiz.models.Country
import com.kodeco.android.worldcountriesquiz.network.CountryService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CountryRepositoryImplTest {

    private lateinit var repository: CountryRepositoryImpl
    private lateinit var mockService: CountryService
    private lateinit var mockDao: CountryDao
    private lateinit var mockPrefs: SettingsPrefs
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        mockService = mockk(relaxed = true)
        mockDao = mockk(relaxed = true)
        mockPrefs = mockk(relaxed = true)

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        coEvery { mockPrefs.getLocalStorageEnabled() } returns flowOf(true)
        coEvery { mockDao.getNeedsReviewCountries() } returns listOf(Country("Spain", "Madrid", 47000000, 505992f, "https://example.com/flag_spain.png", true))
        coEvery { mockService.getAllCountries() } returns Response.success(listOf(Country("France", "Paris", 67000000, 640679f, "https://example.com/flag_france.png", false)))

        repository = CountryRepositoryImpl(mockService, mockDao, mockPrefs)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testFetchCountriesSuccessfully() = dispatcher.runBlockingTest {
        repository.fetchCountries()

        coVerify(exactly = 1) { mockService.getAllCountries() }
        coVerify { mockDao.deleteAllCountries() }
        coVerify { mockDao.insertCountries(any()) }
        assert(repository.countries.value.size == 1)
        assertEquals(repository.countries.value.first().commonName, "France")
    }

    @Test
    fun testFetchCountriesFailure() = dispatcher.runBlockingTest {
        // Arrange
        coEvery { mockService.getAllCountries() } returns Response.error(404, "error".toResponseBody())
        coEvery { mockPrefs.getLocalStorageEnabled() } returns flowOf(true)
        val localCountries = listOf(Country("Italy", "Rome", 60000000, 301338f, "https://example.com/flag_italy.png", false))
        coEvery { mockDao.getAllCountries() } returns localCountries

        // Act & Assert
        repository.fetchCountries()

        // Verify service was called
        coVerify(exactly = 1) { mockService.getAllCountries() }
        // Verify fallback to local data if network call fails
        coVerify { mockDao.getAllCountries() }
        // Ensure the correct local data is set to the flow
        assertEquals(localCountries, repository.countries.value)
    }

    @Test
    fun testFetchCountriesWithLocalStorageDisabled() = dispatcher.runBlockingTest {
        coEvery { mockPrefs.getLocalStorageEnabled() } returns flowOf(false)

        repository.fetchCountries()

        coVerify { mockService.getAllCountries() }
        coVerify(inverse = true) { mockDao.deleteAllCountries() }
        coVerify(inverse = true) { mockDao.insertCountries(any()) }
        coVerify(inverse = true) { mockDao.getNeedsReviewCountries() }
    }

    @Test
    fun testFetchCountriesWithNetworkFailureFallbackToLocal() = dispatcher.runBlockingTest {
        coEvery { mockService.getAllCountries() } returns Response.error(500, "Server Error".toResponseBody())

        repository.fetchCountries()

        coVerify { mockDao.getAllCountries() } // Ensure fallback to local storage is invoked
        assertTrue(repository.countries.value.isEmpty()) // Depending on mock setup
    }

    @Test
    fun testSetNeedsReview() = dispatcher.runBlockingTest {
        // Given
        val country = Country("France", "Paris", 67000000, 640679f, "https://example.com/flag_france.png", false)

        // When
        repository.setNeedsReview(country)

        // Then
        coVerify { mockDao.updateCountry(country.copy(needsReview = true)) }
    }

    @Test
    fun testSearchCountries() = dispatcher.runBlockingTest {
        // Given
        val query = "France"
        val searchResult = listOf(Country("France", "Paris", 67000000, 640679f, "https://example.com/flag_france.png", false))
        coEvery { mockDao.searchCountries(query) } returns searchResult

        // When
        repository.searchCountries(query)

        // Then
        assertEquals(searchResult, repository.countries.value)
        coVerify { mockDao.searchCountries(query) }
    }

    @Test
    fun testToggleNeedsReview() = dispatcher.runBlockingTest {
        // Given

        coEvery { mockService.getAllCountries() } returns Response.error(404, "error".toResponseBody())
        coEvery { mockPrefs.getLocalStorageEnabled() } returns flowOf(true)

        val initialCountry = Country("Germany", "Berlin", 83000000, 357022f, "https://example.com/flag_germany.png", false)
        val updatedCountry = initialCountry.copy(needsReview = !initialCountry.needsReview)
        val initialCountries = listOf(initialCountry)
        // Assuming fetchCountries would typically set this, or we mock the outcome
        coEvery { mockDao.getAllCountries() } returns initialCountries

        repository.fetchCountries() // This should set _countries internally, check how your implementation behaves

        // When
        repository.toggleNeedsReview(initialCountry)

        // Then
        coVerify { mockDao.updateCountry(updatedCountry) }
        assertTrue(repository.countries.value.contains(updatedCountry))
        assertEquals(true, repository.countries.value.first().needsReview)
    }
}