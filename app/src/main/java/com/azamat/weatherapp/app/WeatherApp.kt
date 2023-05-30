package com.azamat.weatherapp.app

import android.app.Application

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinConfig.start(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        KoinConfig.stop()
    }

}