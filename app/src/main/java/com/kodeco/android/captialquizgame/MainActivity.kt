package com.kodeco.android.captialquizgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.kodeco.android.captialquizgame.database.CountryDatabase
import com.kodeco.android.captialquizgame.database.SettingsPrefs
import com.kodeco.android.captialquizgame.database.SettingsPrefsImpl
import com.kodeco.android.captialquizgame.network.CountryService
import com.kodeco.android.captialquizgame.network.adapters.CountryAdapter
import com.kodeco.android.captialquizgame.repositories.CountryRepository
import com.kodeco.android.captialquizgame.repositories.CountryRepositoryImpl
import com.kodeco.android.captialquizgame.ui.nav.CountryInfoNavHost
import com.kodeco.android.captialquizgame.ui.theme.MyApplicationTheme
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
