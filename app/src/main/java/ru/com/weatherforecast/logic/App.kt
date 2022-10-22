package ru.com.weatherforecast.logic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import ru.com.weatherforecast.data.api.OpenWeatherService


class App : Application() {

    companion object {
        lateinit var locationManager: LocationManager
        lateinit var openWeatherService: OpenWeatherService
    }

    @SuppressLint("ServiceCast")
    override fun onCreate() {
        super.onCreate()
        openWeatherService = OpenWeatherService()




        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager



    }



}