package ru.com.weatherforecast.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.core.app.ActivityCompat
import ru.com.weatherforecast.R
import ru.com.weatherforecast.logic.App
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    var currentLocation: Location? = null
    private lateinit var locationListener: LocationListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view = super.onCreateView(name, context, attrs)
        subscribeLocationListener()

        return view
    }

    private fun subscribeLocationListener(): Unit{
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentLocation = location
            }

            override fun onProviderDisabled(provider: String) {
                super.onProviderDisabled(provider)
            }

            override fun onProviderEnabled(provider: String) {


                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                }

                currentLocation = locationManager.getLastKnownLocation(provider)!!
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1000f,locationListener)
    }


}