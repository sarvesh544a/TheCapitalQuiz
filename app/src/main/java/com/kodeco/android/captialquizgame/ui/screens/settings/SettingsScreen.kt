package com.kodeco.android.captialquizgame.ui.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kodeco.android.worldcountriesquiz.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(viewModel: SettingsViewModel,
                   onNavigateUp: () -> Unit,
) {
    val enableNeedsReview by viewModel.enableNeedsReview.collectAsState()
    val enableDataStorage by viewModel.enableDataStorage.collectAsState()

    androidx.compose.material3.Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = {
                    androidx.compose.material3.Text(text = stringResource(id = R.string.settings_screen_title))
                },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.nav_back_content_description),
                        )
                    }
                }
            )
        },
    )
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Needs Review Feature",
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = enableNeedsReview,
                    onCheckedChange = { viewModel.toggleReviewCountry() },
                    modifier = Modifier.weight(0.3f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Enable Local Storage",
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = enableDataStorage,
                    onCheckedChange = { viewModel.toggleDataStorage() },
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
    }
}