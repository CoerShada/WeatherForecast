package ru.com.weatherforecast.data.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.com.weatherforecast.Weather


interface OpenWeatherAPI {

    @GET("./data/2.5/weather")
    fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("part") part: String,@Query("appid")key: String): Single<Weather>

    @GET("./data/2.5/weather")
    fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid")key: String): Single<Weather>
}