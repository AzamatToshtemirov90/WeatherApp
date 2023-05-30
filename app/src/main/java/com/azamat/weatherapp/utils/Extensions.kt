package com.azamat.weatherapp.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.azamat.weatherapp.BuildConfig
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

const val IMAGE_FORMAT_EXT = "@4x.png"


fun Int.unixTimestampToTimeString(): String {

    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}


fun Double.kelvinToCelsius(): Int {
    return (this - 273.15).toInt()
}

fun ImageView.loadImage(
    imageUrl: String
) {
    Glide.with(this)
        .load(imageUrl)
        .into(this)
}

fun String.getIconUrl(): String {
    return BuildConfig.BASE_ICON_URL.plus(this).plus(IMAGE_FORMAT_EXT)
}