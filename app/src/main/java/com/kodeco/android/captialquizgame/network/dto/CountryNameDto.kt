package com.kodeco.android.captialquizgame.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryNameDto(val common: String)
