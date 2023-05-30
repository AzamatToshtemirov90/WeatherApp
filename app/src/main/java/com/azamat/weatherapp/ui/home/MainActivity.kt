package com.azamat.weatherapp.ui.home

import android.os.Bundle
import com.azamat.weatherapp.R
import com.azamat.weatherapp.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeNavController(R.id.main)
    }
}