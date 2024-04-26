package com.kodeco.android.captialquizgame.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kodeco.android.captialquizgame.models.Country
import com.kodeco.android.worldcountriesquiz.R

@Composable
fun CountryInfoList(
    countries: List<Country>,
    onRefreshTap: () -> Unit,
    onCountryRowTap: (countryIndex: Int) -> Unit,
    onCountryNeedsReview: (country: Country) -> Unit,
    enableNeedsReview: Boolean,
) {
    if (countries.isEmpty()) {
        Log.d("CountryInfoList", "Countries Not found")
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = onRefreshTap) {
                Text(text = stringResource(id = R.string.country_info_refresh_button_text))
            }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(countries) { index, country ->
                CountryInfoRow(
                    country = country,
                    onTap = {
                        onCountryRowTap(index)
                    },
                    onNeedsReview = {
                        onCountryNeedsReview(country)
                    },
                    enableNeedsReview
                )
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Preview
@Composable
fun CountryInfoListPreview() {
    val countries = listOf(
        Country("Country 1", "Capital 1", 1000000L, 50000.0f, "FlagUrl1", false),
        Country("Country 2", "Capital 2", 2000000L, 60000.0f, "FlagUrl2", true),
        Country("Country 3", "Capital 3", 1500000L, 55000.0f, "FlagUrl3", false)
        // Add more sample countries as needed
    )

        CountryInfoList(
            countries = countries,
            onRefreshTap = {},
            onCountryRowTap = {},
            onCountryNeedsReview = {},
            enableNeedsReview = true
        )
}