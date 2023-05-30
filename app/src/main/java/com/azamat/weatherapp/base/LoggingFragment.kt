package com.azamat.weatherapp.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewbinding.BuildConfig
import com.azamat.weatherapp.utils.LargeBundleDetector

open class LoggingFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate savedInstanceState=${savedInstanceState != null}")
        LargeBundleDetector.check("onCreate args", arguments)
        LargeBundleDetector.check("onCreate inst", savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        log("onStart")
    }

    override fun onResume() {
        super.onResume()
        log("onResume")
    }

    override fun onPause() {
        super.onPause()
        log("onPause")
    }

    override fun onStop() {
        super.onStop()
        log("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        log("onSaveInstanceState")
        LargeBundleDetector.check("onSaveInstanceState", outState)
    }

    private fun log(message: String){
        if (BuildConfig.DEBUG){
            Log.v(this.javaClass.simpleName, "$message ${hashCode()}")
        }
    }
}