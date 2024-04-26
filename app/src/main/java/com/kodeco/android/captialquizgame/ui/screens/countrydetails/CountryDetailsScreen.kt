package com.kodeco.android.captialquizgame.ui.screens.countrydetails

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kodeco.android.captialquizgame.models.Country
import com.kodeco.android.captialquizgame.repositories.CountryRepository
import com.kodeco.android.captialquizgame.sample.sampleCountries
import com.kodeco.android.captialquizgame.sample.sampleCountry
import com.kodeco.android.captialquizgame.ui.components.CountryDetails
import com.kodeco.android.captialquizgame.ui.components.Loading
import com.kodeco.android.captialquizgame.ui.components.Error
import com.kodeco.android.worldcountriesquiz.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDetailsScreen(
    countryIndex: Int,
    viewModel: CountryDetailsViewModel,
    onNavigateUp: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = "getCountryDetails") {
        viewModel.getCountryDetails(countryIndex)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = (uiState.value as? CountryDetailsState.Success)?.country?.commonName.orEmpty())
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.nav_back_content_description),
                        )
                    }
                }
            )
        },
    ) { padding ->
        when (val countryDetailsState = uiState.value) {
            is CountryDetailsState.Loading -> Loading()
            is CountryDetailsState.Success -> {
                val country = countryDetailsState.country
                CountryDetails(
                    country = country,
                    modifier = Modifier.padding(padding),
                )
            }
            is CountryDetailsState.Error -> Error(
                userFriendlyMessageText = stringResource(id = R.string.country_details_error),
                error = countryDetailsState.error,
            )
        }

    }
}

@Preview
@Composable
fun CountryDetailsScreenPreview() {
    CountryDetailsScreen(
        countryIndex = 0,
        viewModel = CountryDetailsViewModel(
            repository = object : CountryRepository {
                override val countries: Flow<List<Country>>
                    get() = MutableStateFlow(sampleCountries).asStateFlow()

                override suspend fun fetchCountries() {}

                override fun getCountry(index: Int): Country = sampleCountry
                override suspend fun getRandonCountries(count: Int): List<Country> {
                    TODO("Not yet implemented")
                }

                override suspend fun getRandomCapitals(
                    correctCapital: String,
                    limit: Int
                ): List<String> {
                    TODO("Not yet implemented")
                }

                override suspend fun toggleNeedsReview(country: Country) {
                    TODO("Not yet implemented")
                }

                override suspend fun setNeedsReview(country: Country) {
                    TODO("Not yet implemented")
                }

                override suspend fun searchCountries(query: String) {
                    TODO("Not yet implemented")
                }

                suspend fun needsReview(country: Country) {}
            },
        ),
        onNavigateUp = {},
    )
}
