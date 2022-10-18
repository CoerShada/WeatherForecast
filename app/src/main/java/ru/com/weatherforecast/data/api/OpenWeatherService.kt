package ru.com.weatherforecast.data.api

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class OpenWeatherService {
    companion object {
        var API_KEY : String = "d77e2ae18df1bb62b5f29b0e87e9efa6";
        var API_URL : String = "https://api.openweathermap.org"
    }

    val api : OpenWeatherAPI

    init {
        var retrofit = createRetrofit()
        api = retrofit.create(OpenWeatherAPI::class.java)
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(createOkHttpClient())
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    }



}






