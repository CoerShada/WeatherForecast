package ru.com.weatherforecast.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.location.Location
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import ru.com.weatherforecast.R
import ru.com.weatherforecast.data.api.OpenWeatherAPI
import ru.com.weatherforecast.data.api.OpenWeatherService
import ru.com.weatherforecast.logic.App
import java.lang.Thread.sleep


class FragmentMain: Fragment() {


    //Перенести во вью модель
    var disposable: CompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        GlobalScope.launch(Dispatchers.Main) {
            isLocationReady(view)
        }
        return view
    }

    private suspend fun isLocationReady(view: View) = coroutineScope{

        var loading: ImageView = view.findViewById(R.id.iv_mainfragment_loading)
        var textView: TextView = view.findViewById(R.id.test_tv)


        while ((activity as MainActivity).currentLocation==null) {
            delay(100)
            loading.rotation+=36
        }
        loading.visibility = View.GONE
        textView.visibility = View.VISIBLE
        val app : App = activity?.application as App
        val loc : Location = (activity as MainActivity).currentLocation!!
        val lat = loc.latitude
        val lon = loc.longitude
        textView.text = "lat $lat lon $lon"

        disposable.add(app.openWeatherService
            .api.getWeather(lat, lon, OpenWeatherService.API_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textView.text = "Success"
            },{
                textView.text = "Error"

            }))
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()

    }




}