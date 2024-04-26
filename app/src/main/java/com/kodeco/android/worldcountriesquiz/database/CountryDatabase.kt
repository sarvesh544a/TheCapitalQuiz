package com.kodeco.android.worldcountriesquiz.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kodeco.android.worldcountriesquiz.models.Country

private const val DATABASE_NAME = "country_database"
private const val DATABASE_VERSION = 1

@Database(
    entities = [Country::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao
}