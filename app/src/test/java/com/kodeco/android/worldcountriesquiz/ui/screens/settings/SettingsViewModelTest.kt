package com.kodeco.android.worldcountriesquiz.ui.screens.settings

import com.kodeco.android.worldcountriesquiz.database.SettingsPrefs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var mockSettingsPrefs: SettingsPrefs
    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        println("Setting up test")
        Dispatchers.setMain(dispatcher)
        mockSettingsPrefs = mockk(relaxed = true)
        viewModel = SettingsViewModel(mockSettingsPrefs)

        // Mock the behavior of the settings preferences
        coEvery { mockSettingsPrefs.getLocalStorageEnabled() } returns flowOf(true)
        coEvery { mockSettingsPrefs.getReviewCountryEnabled() } returns flowOf(true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testInitialState() = dispatcher.runBlockingTest {
        // Test initial state assumptions
        assertTrue("Data Storage should initially be enabled", viewModel.enableDataStorage.value)
        assertTrue("Review Country should initially be enabled", viewModel.enableNeedsReview.value)
    }

    @Test
    fun toggleReviewCountryFeature() = dispatcher.runBlockingTest {
        // Action: Toggle the feature off
        viewModel.toggleReviewCountry()

        // Verify the preference interaction
        coVerify { mockSettingsPrefs.toggleReviewCountryFeature() }
    }

    @Test
    fun toggleDataStorageFeature() = dispatcher.runBlockingTest {
        // Action: Toggle the feature off
        viewModel.toggleDataStorage()

        // Verify the preference interaction
        coVerify { mockSettingsPrefs.toggleLocalStorage() }
    }
}