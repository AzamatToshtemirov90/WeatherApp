package com.azamat.weatherapp.di


import com.azamat.weatherapp.model.remote.OkHttpClientFactory
import com.azamat.weatherapp.model.remote.RetrofitClient
import com.azamat.weatherapp.utils.ResponseHandler
import org.koin.dsl.module

val networkModule = module {
    single { RetrofitClient(OkHttpClientFactory().create()).retrofit }
    factory { ResponseHandler() }
}