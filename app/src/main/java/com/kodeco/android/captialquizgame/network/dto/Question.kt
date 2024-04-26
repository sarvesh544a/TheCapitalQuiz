package com.kodeco.android.captialquizgame.network.dto

import com.kodeco.android.captialquizgame.models.Country

data class Question(
    val country: Country,
    val options: List<String>,
    val correctAnswer: String,
    var submitted: Boolean = false
)