package com.kodeco.android.captialquizgame.ui.screens.countrylist

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

import com.kodeco.android.captialquizgame.database.SettingsPrefs
import com.kodeco.android.captialquizgame.ui.components.CountryInfoList
import com.kodeco.android.captialquizgame.ui.components.Loading
import com.kodeco.android.captialquizgame.ui.components.Error
import com.kodeco.android.worldcountriesquiz.R

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun CountryListScreen(
    viewModel: CountryListViewModel,
    onCountryRowTap: (countryIndex: Int) -> Unit,
    worldCountriesPrefs: SettingsPrefs,
    navigateToStartScreen: () -> Unit // Function to navigate to StartScreen
) {
    val uiState by viewModel.uiState.collectAsState() // main state for UI updates
    var searchQuery by remember { mutableStateOf("") }
    var enableNeedsReview  = remember { mutableStateOf(true) }
    var showReviewedOnly = remember { mutableStateOf(false) } // State to toggle reviewed countries view


    LaunchedEffect(key1 = worldCountriesPrefs) {
        worldCountriesPrefs.getReviewCountryEnabled().collect { value ->
            enableNeedsReview.value = value
        }
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
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SearchRow(searchQuery, { searchQuery = it }, { viewModel.searchCountries(searchQuery) }, enableNeedsReview = enableNeedsReview, showReviewedOnly = showReviewedOnly)
            Spacer(modifier = Modifier.height(8.dp))
            when (val state = uiState) {
                is CountryListState.Loading -> Loading()
                is CountryListState.Success -> {
                    if (enableNeedsReview.value) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = stringResource(id = R.string.country_info_star_explanation_text), style = MaterialTheme.typography.labelMedium)
                    }
                    CountryInfoList(
                        countries = if (showReviewedOnly.value) state.countries.filter { it.needsReview } else state.countries,
                        onRefreshTap = viewModel::fetchCountries,
                        onCountryRowTap = onCountryRowTap,
                        onCountryNeedsReview = viewModel::toggleNeedsReview,
                        enableNeedsReview = enableNeedsReview.value
                    )
                }
                is CountryListState.Error -> Error(
                    userFriendlyMessageText = stringResource(id = R.string.country_info_error),
                    error = state.error,
                    onRetry = viewModel::fetchCountries,
                )
            }
        }
    }
}

@Composable
fun SearchRow(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    enableNeedsReview: MutableState<Boolean>,
    showReviewedOnly: MutableState<Boolean>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                label = { Text("Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (searchQuery.isNotBlank()) {
                        onSearchClick()
                        defaultKeyboardAction(ImeAction.Search) // This will close the keyboard
                    }
                }),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSearchClick,
            ) {
                Text("Search")
            }
            if (enableNeedsReview.value) {
                IconButton(onClick = { showReviewedOnly.value = !showReviewedOnly.value }) {
                    Icon(
                        imageVector = if (showReviewedOnly.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Reviewed"
                    )
                }
            }
        }
    )
}
