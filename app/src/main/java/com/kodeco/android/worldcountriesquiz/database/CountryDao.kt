package com.kodeco.android.worldcountriesquiz.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kodeco.android.worldcountriesquiz.models.Country

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(country: Country)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountries(countries: List<Country>)

    @Query("SELECT * FROM countries WHERE commonName = :commonName")
    suspend fun getCountry(commonName: String): Country

    @Query("SELECT * FROM countries")
    suspend fun getAllCountries() : List<Country>

    @Query("SELECT * FROM countries WHERE needsReview = 1")
    suspend fun getNeedsReviewCountries(): List<Country>

    @Query("DELETE FROM countries")
    suspend fun deleteAllCountries()

    @Update
    suspend fun updateCountry(country: Country)

    @Query("SELECT * FROM countries WHERE needsReview != 1 ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomCountries(count: Int): List<Country>

    @Query("SELECT mainCapital FROM countries WHERE mainCapital != :correctCapital ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomCapitals(correctCapital: String, limit: Int): List<String>

    @Query("SELECT * FROM countries WHERE commonName LIKE '%' || :query || '%'")
    suspend fun searchCountries(query: String): List<Country>
}