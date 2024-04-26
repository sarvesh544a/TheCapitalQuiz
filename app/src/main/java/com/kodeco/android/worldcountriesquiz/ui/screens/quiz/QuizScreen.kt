package com.kodeco.android.worldcountriesquiz.ui.screens.quiz

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kodeco.android.worldcountriesquiz.network.dto.Question
import com.kodeco.android.worldcountriesquiz.ui.screens.Screen
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.kodeco.android.worldcountriesquiz.R
import com.kodeco.android.worldcountriesquiz.ui.components.NeedsReviewStar


@Composable
fun QuizScreen(
    navController: NavController,
    quizViewModel: QuizViewModel,
    navigateToStartScreen: () -> Unit // Function to navigate to StartScreen
) {
    val isLoading by quizViewModel.isLoading.collectAsState()  // Observe isLoading

    val showAnswerMessage = remember { mutableStateOf(false) }
    val isAnswerCorrect = remember { mutableStateOf(false) }
    val backgroundColor = MaterialTheme.colors.background
    val isCountryMarkedForReview = remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        quizViewModel.startOrResetQuiz()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.country_info_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = navigateToStartScreen) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Confirm exit or simply navigate away
                        navController.navigate(Screen.Start.path)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit Quiz")
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(color = backgroundColor).padding(padding),)
        {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                QuizContent(
                    quizViewModel = quizViewModel,
                    isLoading = isLoading, // Pass isLoading to QuizContent
                    showAnswerMessage = showAnswerMessage,
                    isAnswerCorrect = isAnswerCorrect,
                    isCountryMarkedForReview
                )
            }
        }
    }
}

@Composable
fun QuizContent(
    quizViewModel: QuizViewModel,
    isLoading: Boolean,  // Accept isLoading as a parameter
    showAnswerMessage: MutableState<Boolean>,
    isAnswerCorrect: MutableState<Boolean>,
    isCountryMarkedForReview:  MutableState<Boolean>
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (quizViewModel.currentQuestion != null) {
            QuizCard(quizViewModel, showAnswerMessage, isAnswerCorrect, isCountryMarkedForReview)
        } else {
                QuizCompletionScreen(quizViewModel)
        }

        AnswerMessageSection(showAnswerMessage, isAnswerCorrect, quizViewModel.currentQuestion)
    }
}

@Composable
fun QuizCard(
    quizViewModel: QuizViewModel,
    showAnswerMessage: MutableState<Boolean>,
    isAnswerCorrect: MutableState<Boolean>,
    isCountryMarkedForReview:  MutableState<Boolean>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            QuestionHeader(quizViewModel)
            QuestionContent(quizViewModel, isCountryMarkedForReview)
            OptionsList(quizViewModel)
            AnswerButtons(quizViewModel, showAnswerMessage, isAnswerCorrect, isCountryMarkedForReview)
            ReviewButtons(quizViewModel, isCountryMarkedForReview)
        }
    }
}

@Composable
fun QuestionHeader(quizViewModel: QuizViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Question ${quizViewModel.currentQuestionIndex.value + 1} of ${quizViewModel.totalQuestions}",
            style = MaterialTheme.typography.h6
        )

        Text(
            text = "Score: ${quizViewModel.score.value}",
            style = MaterialTheme.typography.h6.copy(color = Color.Blue),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun QuestionContent(quizViewModel: QuizViewModel, isCountryMarkedForReview:  MutableState<Boolean>) {
    val currentQuestion = quizViewModel.currentQuestion
    if (currentQuestion != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            // Display the star icon next to the question if it is marked for review
            Text(
                text = "What is the capital of ${currentQuestion.country.commonName}?",
                style = MaterialTheme.typography.h6
            )
            if (isCountryMarkedForReview.value) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Marked for review",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically)

                )
            }
        }
    }
}

@Composable
fun OptionsList(quizViewModel: QuizViewModel) {
    val options = quizViewModel.currentQuestion?.options ?: listOf()
    Column(modifier = Modifier.fillMaxWidth()) {
        for (option in options) {
            OptionCard(
                option = option,
                isSelected = quizViewModel.selectedOption.value == option,
                onSelect = {
                    if (!quizViewModel.isSubmitted.value) {
                        quizViewModel.selectedOption.value = option
                    }
                },
                isSubmitted = quizViewModel.isSubmitted.value
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun OptionCard(
    option: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    isSubmitted: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isSubmitted, onClick = onSelect),
        elevation = 4.dp,
        backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.1f) else MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null, // nullify the click here to handle from card
                enabled = !isSubmitted
            )
            Text(
                text = option,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun AnswerButtons(
    quizViewModel: QuizViewModel,
    showAnswerMessage: MutableState<Boolean>,
    isAnswerCorrect: MutableState<Boolean>,
    isCountryMarkedForReview: MutableState<Boolean>
) {
    var enableButtonValue: Boolean
    if (!quizViewModel.isSubmitted.value) {
        if (quizViewModel.selectedOption.value != "null")
            enableButtonValue = true
        else
            enableButtonValue = false

        Button(
            onClick = {
                showAnswerMessage.value = true
                isAnswerCorrect.value = quizViewModel.submitAnswer()
            },
            enabled = enableButtonValue,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    } else {
        Button(
            onClick = {
                showAnswerMessage.value = false
                isCountryMarkedForReview.value = false
                quizViewModel.moveToNextQuestion()
                quizViewModel.selectedOption.value = null.toString()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next Question")
        }
    }
}

@Composable
fun ReviewButtons(
    quizViewModel: QuizViewModel,
    isCountryMarkedForReview: MutableState<Boolean>
) {
    Button(
        onClick = {
            quizViewModel.markCountryAsNeedsReview()
            isCountryMarkedForReview.value = !isCountryMarkedForReview.value
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Mark Country as Needs Review")
    }
}


@Composable
fun AnswerMessageSection(
    showAnswerMessage: MutableState<Boolean>,
    isAnswerCorrect: MutableState<Boolean>,
    currentQuestion: com.kodeco.android.worldcountriesquiz.network.dto.Question?
) {
    if (showAnswerMessage.value) {
        val title = if (isAnswerCorrect.value) "Answer is correct!" else "Wrong Answer!"
        val message = if (!isAnswerCorrect.value) "The correct answer is: ${currentQuestion?.correctAnswer}" else ""

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.h6)
            if (!isAnswerCorrect.value) {
                Text(text = message, style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Composable
fun QuizCompletionScreen(quizViewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Final Score: ${quizViewModel.score.value} / ${quizViewModel.totalQuestions}",
            style = MaterialTheme.typography.h5
        )
    }
}

