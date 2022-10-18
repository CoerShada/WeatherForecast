package ru.com.weatherforecast.logic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import ru.com.weatherforecast.data.api.OpenWeatherAPI
import ru.com.weatherforecast.data.api.OpenWeatherService


class App : Application() {

    lateinit var openWeatherService : OpenWeatherService


    @SuppressLint("ServiceCast")
    override fun onCreate() {
        super.onCreate()
        openWeatherService = OpenWeatherService()

    }
}