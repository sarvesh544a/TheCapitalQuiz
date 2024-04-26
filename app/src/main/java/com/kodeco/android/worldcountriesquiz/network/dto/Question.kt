package com.kodeco.android.worldcountriesquiz.network.dto

import com.kodeco.android.worldcountriesquiz.models.Country

data class Question(
    val country: Country,
    val options: List<String>,
    val correctAnswer: String,
    var submitted: Boolean = false
)