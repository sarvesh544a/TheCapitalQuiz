package com.kodeco.android.capitalquizgame.network.adapters


import com.kodeco.android.capitalquizgame.models.Country
import com.kodeco.android.capitalquizgame.network.dto.CountryDto
import com.kodeco.android.capitalquizgame.network.dto.CountryFlagsDto
import com.kodeco.android.capitalquizgame.network.dto.CountryNameDto
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class CountryAdapterTest {
    private val adapter = CountryAdapter()

    @Test
    fun `test fromJson converts CountryDto list to Country list`() {
        // Given
        val countryDtos = listOf(
            CountryDto(
                name = CountryNameDto(common = "France"),
                capital = listOf("Paris"),
                population = 67000000,
                area = 643801.0f,
                flags = CountryFlagsDto(png = "https://example.com/france.png", svg = "")
            )
        )

        // When
        val countries = adapter.fromJson(countryDtos)

        // Then
        assertEquals(1, countries.size)
        with(countries.first()) {
            assertEquals("France", commonName)
            assertEquals("Paris", mainCapital)
            assertEquals(67000000, population)
            assertEquals("delta difference", 643801.0, area.toDouble(), 0.001)
            assertEquals("https://example.com/france.png", flagUrl)
        }
    }

    @Test
    fun `test toJson converts Country list to CountryDto list`() {
        // Given
        val countries = listOf(
            Country(
                commonName = "Germany",
                mainCapital = "Berlin",
                population = 83000000,
                area = 357022.0f,
                flagUrl = "https://example.com/germany.png"
            )
        )

        // When
        val countryDtos = adapter.toJson(countries)

        // Then
        assertEquals(1, countryDtos.size)
        with(countryDtos.first()) {
            assertEquals("Germany", name.common)
            capital?.let { assertTrue(it.contains("Berlin")) }
            assertEquals(83000000, population)
            assertEquals("delta difference", 357022.0, area.toDouble(), 0.001)
            assertEquals("https://example.com/germany.png", flags.png)
            assertTrue(flags.svg.isEmpty())
        }
    }
}