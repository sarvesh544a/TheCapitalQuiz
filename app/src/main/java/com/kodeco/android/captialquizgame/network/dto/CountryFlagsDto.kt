package com.kodeco.android.captialquizgame.network.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryFlagsDto(val png: String, val svg: String)
