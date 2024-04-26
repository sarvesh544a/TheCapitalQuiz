package com.kodeco.android.worldcountriesquiz.sample

import com.kodeco.android.worldcountriesquiz.models.Country

val sampleCountries = listOf(
    Country(
        commonName = "United States",
        mainCapital = "Washington, D.C.",
        population = 328_239_523,
        area = 9_833_520f,
        flagUrl = "",
    ),
    Country(
        commonName = "Canada",
        mainCapital = "Ottawa",
        population = 37_742_154,
        area = 9_984_670f,
        flagUrl = "",
    ),
    Country(
        commonName = "Mexico",
        mainCapital = "Mexico City",
        population = 126_014_024,
        area = 1_964_375f,
        flagUrl = "",
    ),
    Country(
        commonName = "Germany",
        mainCapital = "Berlin",
        population = 83_517_045,
        area = 137_847f,
        flagUrl = "",
    ),
)

val sampleCountry = Country(
    commonName = "France",
    mainCapital = "Paris",
    population = 67000000,
    area = 640679f,
    flagUrl = "https://example.com/flag_france.png",
    needsReview = false
)
