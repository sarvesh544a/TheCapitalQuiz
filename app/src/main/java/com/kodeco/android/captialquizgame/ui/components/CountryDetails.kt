package com.kodeco.android.captialquizgame.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kodeco.android.captialquizgame.models.Country
import com.kodeco.android.captialquizgame.sample.sampleCountry
import java.text.NumberFormat
import java.util.*

@Composable
fun CountryDetails(
    country: Country,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Capital: ${country.mainCapital}",
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        item {
            Text(
                text = "Population: ${NumberFormat.getNumberInstance(Locale.US).format(country.population)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Text(
                text = "Area: ${NumberFormat.getNumberInstance(Locale.US).format(country.area)} km²",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            FlagImage(country = country)
        }
    }
}

@Composable
private fun FlagImage(country: Country) {
    var expanded by remember { mutableStateOf(false) }
    val flagTransition = updateTransition(
        targetState = expanded,
        label = "${country.commonName}_details_transition"
    )
    val widthAnimation by flagTransition.animateDp(
        label = "${country.commonName}_details_size"
    ) { state ->
        if (state) 300.dp else 150.dp
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(country.flagUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Flag",
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(10.dp))
            .width(widthAnimation)
            .clickable { expanded = !expanded }
    )
}


@Preview(showBackground = true, name = "Country Details Preview")
@Composable
fun PreviewCountryDetails() {
    // Use the CountryDetails composable to render the preview
    CountryDetails(
        country = sampleCountry,
        modifier = Modifier.fillMaxWidth() // Adjust as necessary for the preview
    )
}