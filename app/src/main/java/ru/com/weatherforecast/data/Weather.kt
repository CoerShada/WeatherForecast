package ru.com.weatherforecast

import com.google.gson.annotations.SerializedName

data class Weather(
    val lon: Float,
    val lat: Float,
    @SerializedName("weather")
    val weatherItems: List<WeatherItem>,
    val visibility: Int,
    val wind: Wind,
    @SerializedName("main")
    val mainInfo: MainInfo,
    @SerializedName("dt")
    val receivedIn: Long,
    val timezone: Long,
    @SerializedName("name")
    val cityName: String,
    )

data class WeatherItem(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainInfo(
    @SerializedName("temp")
    val temperature: Float,
    @SerializedName("feels_like")
    val temperatureFeelsLike: Float,
    val pressure: Int,
    val humidity: Byte,
)

data class Wind(
    val speed: Float,
    @SerializedName("deg")
    val direction: Int,
    val gust: Float


){
    fun getDirectionShortStringId(): Int{


        return when (direction){
            in 23..67-> R.string.nord_ost_s
            in 68..112-> R.string.ost_s
            in 113..157-> R.string.south_ost_s
            in 158..202-> R.string.south_s
            in 203..247-> R.string.south_west_s
            in 248..292-> R.string.west_s
            in 293..338-> R.string.nord_west_s
            else-> R.string.nord_s
        }





    }
}


enum class CardinalPoints(val index: Byte, val stringRepresentation: String){
    NORD(0, "nord"),
    OST(1, "ost"),
    SOUTH(2, "south"),
    WEST(3, "west"),
    NORD_OST(4, "nord-ost"),
    SOUTH_OST(5, "south-ost"),
    SOUTH_WEST(6, "south-west"),
    NORD_WEST(7, "nord-west");
}

enum class Сloudiness(val index: Byte, val stringRepresentation: String){
    CLEAR(0, "clear"),
    PARTLY_CLOUDY(1, "partly cloudy"),
    CLOUDY(2, "cloudy"),
    FOG(3, "fog");
}

enum class Precipitations(val index: Byte, val StringRepresentation: String){
    SMALL_RAIN(0, "small rain"),
    RAIN(1, "rain"),
    SHOWER(2, "shower"),
    SNOW_WITH_RAIN(3, "snow with rain"),
    LIGHT_SNOWFALL(4, "light snowfall"),
    SNOWFALL(5, "snowfall")
}