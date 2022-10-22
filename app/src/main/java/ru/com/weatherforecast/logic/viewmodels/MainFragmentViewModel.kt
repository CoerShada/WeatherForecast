package ru.com.weatherforecast.logic.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.com.weatherforecast.R
import ru.com.weatherforecast.Weather
import ru.com.weatherforecast.data.api.OpenWeatherService
import ru.com.weatherforecast.logic.App
import kotlinx.coroutines.*
import ru.com.weatherforecast.logic.LANG
import ru.com.weatherforecast.logic.UNITS
import ru.com.weatherforecast.logic.states.MainFragmentStates

@OptIn(DelicateCoroutinesApi::class)
class MainFragmentViewModel() : ViewModel() {
    var state = MutableLiveData<MainFragmentStates>()
    var currentLocation = MutableLiveData<Location?>()
    var weather = MutableLiveData<Weather>()
    var locationListener = MutableLiveData<LocationListener>()

    var disposable: CompositeDisposable = CompositeDisposable()


    init{
        state.value = MainFragmentStates.DefaultState
        subscribeLocationListener()
        updateLocation()
    }

    fun loadData(){
        state.value = MainFragmentStates.LoadingState

        val loc : Location = currentLocation.value!!
        val lat = loc.latitude
        val lon = loc.longitude
        GlobalScope.launch(Dispatchers.Main) {
            disposable.add(App.openWeatherService
                .api.getCurrentWeather(lat = lat, lon = lon, lang = LANG, units= UNITS, key=OpenWeatherService.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    weather.value = it
                    state.value = MainFragmentStates.LoadingSuccessedState
                }, {
                    state.value = MainFragmentStates.ErrorState

                })
            )
        }
    }

    private fun subscribeLocationListener(){
        locationListener.value = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentLocation.value = location
            }

            @SuppressLint("MissingPermission")
            override fun onProviderEnabled(provider: String) {
                currentLocation.value = App.locationManager.getLastKnownLocation(provider)!!
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(){
        GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                delay(1000)
                App.locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    1000f,
                    locationListener.value!!
                )
            }
        }
    }
}