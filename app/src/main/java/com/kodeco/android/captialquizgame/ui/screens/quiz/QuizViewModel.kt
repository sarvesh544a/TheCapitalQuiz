package com.kodeco.android.captialquizgame.ui.screens.quiz

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kodeco.android.captialquizgame.network.dto.Question
import com.kodeco.android.captialquizgame.repositories.CountryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: CountryRepository,
    private val numQuestions: Int // New parameter to define number of random questions
) : ViewModel() {
    val totalQuestions = numQuestions
    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions = _questions.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val currentQuestionIndex = mutableStateOf(0)
    val score = mutableStateOf(0)
    var selectedOption = mutableStateOf<String?>(null)
    val isSubmitted = mutableStateOf(false)
    val currentQuestion: Question?
        get() = questions.value.getOrNull(currentQuestionIndex.value)

    class QuizViewModelFactory(private val repository: CountryRepository, private val numQuestions: Int) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            QuizViewModel(repository, numQuestions) as T
    }

    // Function to start or reset quiz
    fun startOrResetQuiz() {
        _isLoading.value = true  // Start loading
        _questions.value = emptyList()
        currentQuestionIndex.value = 0
        score.value = 0
        isSubmitted.value = false
        selectedOption.value = null
        viewModelScope.launch {
            fetchQuestions()
            _isLoading.value = false  // End loading after fetching
        }
    }

    fun selectOption(option: String) {
        selectedOption.value = option
    }

    // Function to fetch questions
    private suspend fun fetchQuestions() {
        try {
            val randomCountries = repository.getRandonCountries(numQuestions)
            val newQuestions = randomCountries.map { country ->
                val correctAnswer = country.mainCapital
                val wrongAnswers = repository.getRandomCapitals(correctAnswer, 3)
                val options = (wrongAnswers + correctAnswer).shuffled()
                Question(country, options, correctAnswer)
            }
            _questions.value = newQuestions
        } catch (e: Exception) {
            Log.e("QuizViewModel", "Error fetching questions: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    init {
        viewModelScope.launch {
            repository.fetchCountries()
            startOrResetQuiz()
        }
    }

    fun submitAnswer(): Boolean {
        isSubmitted.value = true
        if (selectedOption.value == currentQuestion?.correctAnswer) {
            score.value++
            return true
        } else return false
    }

    fun markCountryAsNeedsReview() {
        viewModelScope.launch {
            currentQuestion?.country?.let { country ->
                repository.setNeedsReview(country)
            }
        }
    }

    fun moveToNextQuestion() {
        currentQuestionIndex.value++
        selectedOption.value = ""
        isSubmitted.value = false
    }


}
