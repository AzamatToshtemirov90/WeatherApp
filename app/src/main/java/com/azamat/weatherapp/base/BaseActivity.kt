package com.azamat.weatherapp.base

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

open class BaseActivity : LoggingActivity() {

    protected lateinit var navController: NavController

    protected fun initializeNavController(fragmentContainerId: Int) {
        navController =
            supportFragmentManager.findFragmentById(fragmentContainerId)?.findNavController()!!
    }

}