package com.azamat.weatherapp.model.remote.api

import com.azamat.weatherapp.BuildConfig
import com.azamat.weatherapp.model.remote.response.CityNameEntity
import com.azamat.weatherapp.model.remote.response.WeatherEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(BuildConfig.WEATHER_ATTRIBUTE)
    suspend fun getWeatherByLatLong(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") appId: String = BuildConfig.APP_ID
    ): WeatherEntity

    @GET(BuildConfig.CITY_ATTRIBUTE)
    suspend fun searchCityByName(
        @Query("q") searchQuery: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") appId: String = BuildConfig.APP_ID
    ): List<CityNameEntity>
}