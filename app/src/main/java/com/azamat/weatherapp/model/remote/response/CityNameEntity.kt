package com.azamat.weatherapp.model.remote.response

data class CityNameEntity(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)