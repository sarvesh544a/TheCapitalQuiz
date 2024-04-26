package com.kodeco.android.worldcountriesquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.room.Room
import com.kodeco.android.worldcountriesquiz.database.CountryDatabase
import com.kodeco.android.worldcountriesquiz.database.SettingsPrefs
import com.kodeco.android.worldcountriesquiz.database.SettingsPrefsImpl
import com.kodeco.android.worldcountriesquiz.network.CountryService
import com.kodeco.android.worldcountriesquiz.network.adapters.CountryAdapter
import com.kodeco.android.worldcountriesquiz.repositories.CountryRepository
import com.kodeco.android.worldcountriesquiz.repositories.CountryRepositoryImpl
import com.kodeco.android.worldcountriesquiz.ui.nav.CountryInfoNavHost
import com.kodeco.android.worldcountriesquiz.ui.theme.MyApplicationTheme
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            CountryDatabase::class.java,
            "country_database"
        ).build()

        val moshi = Moshi.Builder()
            .add(CountryAdapter())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val service: CountryService = retrofit.create(CountryService::class.java)
        val worldCountriesPrefs: SettingsPrefs = SettingsPrefsImpl(applicationContext)
        val repository: CountryRepository = CountryRepositoryImpl(service, database.countryDao(), worldCountriesPrefs)

        setContent {
            MyApplicationTheme() {
                CountryInfoNavHost(repository = repository, worldCountriesPrefs = worldCountriesPrefs)
            }
        }
    }
}
