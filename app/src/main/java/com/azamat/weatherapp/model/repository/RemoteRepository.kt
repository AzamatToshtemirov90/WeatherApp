package com.azamat.weatherapp.model.repository

import com.azamat.weatherapp.base.BaseApiResult
import com.azamat.weatherapp.model.remote.response.CityNameEntity
import com.azamat.weatherapp.model.remote.response.WeatherEntity

interface RemoteRepository {
    suspend fun getWeatherByLatLong(
        lat: Double,
        long: Double
    ): BaseApiResult<WeatherEntity>

    suspend fun searchCityByName(
        searchQuery: String
    ): BaseApiResult<List<CityNameEntity>>

}