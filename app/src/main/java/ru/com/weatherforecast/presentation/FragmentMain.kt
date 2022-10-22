package ru.com.weatherforecast.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.com.weatherforecast.R
import ru.com.weatherforecast.logic.UNITS
import ru.com.weatherforecast.logic.states.MainFragmentStates
import ru.com.weatherforecast.logic.viewmodels.MainFragmentViewModel
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class FragmentMain: Fragment() {


    lateinit var viewmodel : MainFragmentViewModel
    lateinit var loading: ImageView



    @SuppressLint("SetTextI18n", "SimpleDateFormat", "UseCompatLoadingForDrawables",
        "DiscouragedApi"
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)

        viewmodel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)


        loading = view.findViewById(R.id.iv_fragmentmain_loading)
        val mainLayer: LinearLayout = view.findViewById(R.id.ll_fragmentmain_main_container)

        viewmodel.currentLocation.observe(viewLifecycleOwner){
            if (it!=null)
                viewmodel.loadData()
        }

        viewmodel.state.observe(viewLifecycleOwner) {
            when (it) {
                MainFragmentStates.DefaultState ->{
                    loading.visibility = View.VISIBLE
                    mainLayer.visibility = View.GONE
                }
                MainFragmentStates.LoadingState ->{
                    loading.visibility = View.VISIBLE
                    mainLayer.visibility = View.GONE
                }
                MainFragmentStates.LoadingSuccessedState ->{
                    loading.visibility = View.GONE
                    mainLayer.visibility = View.VISIBLE

                }
                MainFragmentStates.ErrorState ->{
                    loading.visibility = View.GONE
                    mainLayer.visibility = View.GONE
                    throw java.lang.NullPointerException("Empty data")
                }
                else -> {
                    throw IllegalStateException("Unexpected state $it")
                }
            }

            viewmodel.weather.observe(viewLifecycleOwner){
                val weather = viewmodel.weather.value!!
                var tvCityName : TextView = view.findViewById(R.id.tv_fragmentmain_city)
                var tvTemperature : TextView = view.findViewById(R.id.tv_fragmentmain_temperature)
                var tvDescription : TextView = view.findViewById(R.id.tv_fragmentmain_description)
                var tvFeelsLike : TextView = view.findViewById(R.id.tv_fragmentmain_feels_like)
                var tvWind : TextView = view.findViewById(R.id.tv_fragmentmain_wind)
                var tvPressure : TextView = view.findViewById(R.id.tv_fragmentmain_pressure)
                var tvHumidity : TextView = view.findViewById(R.id.tv_fragmentmain_humidity)
                var tvVisibility: TextView = view.findViewById(R.id.tv_fragmentmain_visibility)
                var tvDateTime : TextView = view.findViewById(R.id.tv_fragmentmain_datetime)

                var cvTemperatureColor : CardView = view.findViewById(R.id.cv_fragmentmain_temperaturecolor)

                var imageView: ImageView = view.findViewById(R.id.iv_fragmentmain_main)



                tvCityName.text = weather.cityName
                tvTemperature.text = (weather.mainInfo.temperature.roundToInt()).toString() +
                        "°" + if(UNITS == "metric") "C" else "F"

                tvFeelsLike.text = (weather.mainInfo.temperatureFeelsLike.roundToInt()).toString() +
                        "°" + if(UNITS == "metric") "C" else "F"

                tvDescription.text = weather.weatherItems[0].description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                tvWind.text = getString(weather.wind.getDirectionShortStringId()) + " "+
                        weather.wind.speed +" "+ getString(R.string.m_s)

                tvPressure.text = (weather.mainInfo.pressure / 1.333F).roundToInt().toString()
                tvHumidity.text = weather.mainInfo.humidity.toString() + "%"
                tvVisibility.text = weather.visibility.toString() + getString(R.string.m)

                val formatter = SimpleDateFormat("dd \"MMMM\" yyyy hh:mm")

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = weather.receivedIn*1000
                tvDateTime.text = formatter.format(calendar.time)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    cvTemperatureColor.setBackgroundColor(calculateColor(weather.mainInfo.temperature))
                }

                val imageId = resources.getIdentifier("i${weather.weatherItems[0].icon}", "drawable", activity?.packageName)
                imageView.setImageResource(imageId);

            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        rotateLoader()

    }

    private fun rotateLoader(){

        if (Date().time % 100F == 0F) {
            loading.rotation += 36
        }

        
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateColor(temp: Float): Int{

        val warmColor = Color.rgb(0.6196F, 0.8941F, 0.5412F)
        val zeroColor = Color.rgb(0.52F, 0.52F, 0.52F)
        val coolColor = Color.rgb(0.6196F, 0.9059F, 1F)

        val hotColor = Color.rgb(1F, 0.7294F, 0F)
        val coldColor = Color.rgb(0.94F, 1F, 1F)

        val hotTemp = 15F
        val coldTemp = -15F
        val warmTemp = 0F
        val coolTemp = -5F

        return when (temp){
            in coolTemp..warmTemp-> zeroColor
            in warmTemp..hotTemp-> warmColor
            in hotTemp..Float.MAX_VALUE-> hotColor
            in coolTemp..coldTemp-> coolColor
            else -> {coldColor}
        }
    }







}