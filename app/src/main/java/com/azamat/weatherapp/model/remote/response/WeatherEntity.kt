package com.azamat.weatherapp.model.remote.response

data class WeatherEntity(
    val base: String,
    val clouds: CloudsEntity,
    val cod: Int,
    val coord: CoordEntity?,
    val dt: Int,
    val id: Int,
    val main: MainEntity,
    val name: String,
    val sys: SysEntity,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherDetailEntity>,
    val wind: WindEntity
)

data class CloudsEntity(
    val all: Int
)

data class CoordEntity(
    val lat: Double?,
    val lon: Double?
)

data class MainEntity(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double?,
    val temp_max: Double,
    val temp_min: Double
)

data class SysEntity(
    val country: String,
    val sunrise: Int,
    val sunset: Int
)

data class WeatherDetailEntity(
    val description: String,
    val icon: String?,
    val id: Int,
    val main: String
)

data class WindEntity(
    val deg: Int,
    val gust: Double,
    val speed: Double
)