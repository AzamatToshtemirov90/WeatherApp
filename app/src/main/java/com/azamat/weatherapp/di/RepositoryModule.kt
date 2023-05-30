package com.azamat.weatherapp.di


import com.azamat.weatherapp.model.repository.RemoteRepository
import com.azamat.weatherapp.model.repository.RemoteRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<RemoteRepository> { RemoteRepositoryImpl(get()) }
}