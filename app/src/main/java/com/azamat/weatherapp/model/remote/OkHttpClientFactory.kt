package com.azamat.weatherapp.model.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.azamat.weatherapp.BuildConfig


class OkHttpClientFactory {
    private val OkHttp_TIMEOUT = 5L // connection timeout

    fun create(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        connectTimeout(OkHttp_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(OkHttp_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(OkHttp_TIMEOUT, TimeUnit.SECONDS)
        addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
        addInterceptor(Interceptor { chain ->
            chain.proceed(
                chain.request()
                    .newBuilder()
                    .build()
            )
        })
    }
}