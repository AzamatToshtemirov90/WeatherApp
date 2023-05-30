package com.azamat.weatherapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.azamat.weatherapp.base.BaseViewModel
import com.azamat.weatherapp.base.Status
import com.azamat.weatherapp.model.remote.response.CityNameEntity
import com.azamat.weatherapp.model.remote.response.WeatherEntity
import com.azamat.weatherapp.model.repository.RemoteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class HomeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val remoteRepository: RemoteRepository,

) : BaseViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    private var _weatherResponse = MutableLiveData<WeatherEntity>()
    val weatherResponse: LiveData<WeatherEntity> = _weatherResponse
    private var _cityResponse = MutableLiveData<List<CityNameEntity>>()
    val cityResponse: LiveData<List<CityNameEntity>> = _cityResponse

    val onListItemClickListener: (position: Int) -> Unit = { clickedPosition ->
        val cityCountryName = cityResponse.value?.get(clickedPosition)!!
        getWeather(cityCountryName.lat, cityCountryName.lon)
    }

    fun getWeather(lat: Double, lon: Double) {
        viewModelScope.launch(ioDispatcher) {
            remoteRepository.getWeatherByLatLong(lat, lon).let { baseApiResult ->
                when (baseApiResult.status) {
                    Status.ERROR -> {
                        _error.postValue(baseApiResult.message.toString())
                        Log.d(TAG, baseApiResult.message.toString())
                    }
                    Status.SUCCESS -> {
                        baseApiResult.data?.let { it ->
                            _weatherResponse.postValue(it)
                        }
                    }
                }
            }
        }

    }

    fun searchCity(query: String) {
        viewModelScope.launch(ioDispatcher) {
            remoteRepository.searchCityByName(query).let { baseApiResult ->
                when (baseApiResult.status) {
                    Status.ERROR -> {
                        _error.postValue(baseApiResult.message.toString())
                        Log.d(TAG, baseApiResult.message.toString())
                    }
                    Status.SUCCESS -> {
                        baseApiResult.data?.let { it ->
                            _cityResponse.postValue(it.toList())
                        }
                    }
                }
            }
        }
    }
}