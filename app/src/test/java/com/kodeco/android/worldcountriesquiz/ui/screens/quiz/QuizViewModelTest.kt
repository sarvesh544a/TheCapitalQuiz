package com.kodeco.android.worldcountriesquiz.ui.screens.quiz

import com.kodeco.android.worldcountriesquiz.models.Country
import com.kodeco.android.worldcountriesquiz.repositories.CountryRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuizViewModelTest {

    private lateinit var viewModel: QuizViewModel
    private lateinit var mockRepository: CountryRepository
    private var numQuestions: Int = 0
    private val dispatcher = TestCoroutineDispatcher()

    val country = Country(
        commonName = "France",
        mainCapital = "Paris",
        population = 67000000,
        area = 640679f,
        flagUrl = "https://example.com/flag_france.png",
        needsReview = false
    )
    val countryJapan = Country(
        commonName = "Japan",
        mainCapital = "Tokyo",
        population = 126300000,
        area = 377975f,
        flagUrl = "https://example.com/flag_japan.png",
        needsReview = false
    )

    val countryList = listOf(country, countryJapan)

    @Before
    fun setUp() {
        numQuestions = 2
        Dispatchers.setMain(dispatcher)
        mockRepository = mockk(relaxed = true)
        viewModel = QuizViewModel(repository = mockRepository, numQuestions)
        coEvery { mockRepository.getRandonCountries(numQuestions) } returns countryList
        coEvery { mockRepository.getRandomCapitals("Paris", 3) } returns listOf("London", "Berlin", "Rome")
        coEvery { mockRepository.getRandomCapitals("Tokyo", 3) } returns listOf("Beijing", "Seoul", "Bangkok")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun fetchQuestionsSuccessState() = dispatcher.runBlockingTest {
        val loadingStates = mutableListOf<Boolean>()
        val job = launch {
            viewModel.isLoading.collect { isLoading ->
                loadingStates.add(isLoading)
            }
        }
        viewModel.startOrResetQuiz()
        advanceUntilIdle()
        assertTrue("Loading should be observed as started and completed", loadingStates.contains(true) && loadingStates.contains(false))
        assertEquals("There should be 2 questions loaded", 2, viewModel.questions.value.size)
        assertNotNull("Check that the questions contain expected data", viewModel.questions.value.firstOrNull { it.correctAnswer == "Paris" })

        job.cancel()

    }

    @Test
    fun testSubmitCorrectAnswer() = dispatcher.runBlockingTest {
        viewModel.startOrResetQuiz()
        advanceUntilIdle()
        assertTrue("Questions should be loaded", viewModel.questions.value.isNotEmpty())
        viewModel.selectOption("Paris")
        val result = viewModel.submitAnswer()
        assertTrue("The answer should be correct.", result)
        assertEquals("The score should be incremented.", 1, viewModel.score.value)
        assertTrue("The question should be marked as submitted.", viewModel.isSubmitted.value)
    }
}