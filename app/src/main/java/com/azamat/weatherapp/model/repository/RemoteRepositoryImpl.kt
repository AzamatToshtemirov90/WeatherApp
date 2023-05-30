package com.azamat.weatherapp.model.repository

import com.azamat.weatherapp.base.BaseApiResult
import com.azamat.weatherapp.base.BaseRepository
import com.azamat.weatherapp.model.remote.api.ApiService
import com.azamat.weatherapp.model.remote.response.CityNameEntity
import com.azamat.weatherapp.model.remote.response.WeatherEntity

class RemoteRepositoryImpl(private val apiService: ApiService) : RemoteRepository,
    BaseRepository() {

    override suspend fun getWeatherByLatLong(
        lat: Double,
        long: Double
    ): BaseApiResult<WeatherEntity> {
        return safeApi {
            apiService.getWeatherByLatLong(lat, long)
        }

    }

    override suspend fun searchCityByName(
        searchQuery: String
    ): BaseApiResult<List<CityNameEntity>> {
        return safeApi {
            apiService.searchCityByName(searchQuery)
        }

    }
}