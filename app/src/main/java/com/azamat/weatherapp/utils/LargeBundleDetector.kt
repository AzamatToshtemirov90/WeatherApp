package com.azamat.weatherapp.utils

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.viewbinding.BuildConfig

object LargeBundleDetector {

    private val TAG = this::class.java.simpleName
    private const val RISKY_SIZE = 400000

    fun check(methodName: String, bundle: Bundle?) {
        if (BuildConfig.DEBUG) {
            return
        }

        if (bundle == null) {
            return
        }

        val size = sizeOf(bundle)
        if (size > RISKY_SIZE) {
            Log.w(TAG, "$methodName with RISKY (> $RISKY_SIZE) size $size")
            if (bundle.keySet().size > 0) {
                Log.w(TAG, "Bundle Keys and sizes:")
                for (key in bundle.keySet()) {
                    val v = bundle.get(key)
                    if (v is Parcelable) {
                        Log.w(TAG, "\t$key : ${sizeOf(v)}")
                    }
                }
            }
        }
    }

    private fun sizeOf(parcelable: Parcelable): Int {
        val parcel = Parcel.obtain()
        parcelable.writeToParcel(parcel, 0)
        val size = parcel.dataSize()
        parcel.recycle()
        return size
    }
}