package com.azamat.weatherapp.di

import com.azamat.weatherapp.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    factory { Dispatchers.IO }
    viewModel { HomeViewModel(get(), get()) }
}