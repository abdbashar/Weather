package com.example.weather

import org.json.JSONObject
import com.example.weather.weatherData
import org.json.JSONException

class weatherData {
    private var mTemperature: String? = null
    var micon: String? = null
        private set
    var mcity: String? = null
        private set
    private var mWeatherType: String? = null
    private var mCondition = 0
    fun getmTemperature(): String {
        return "$mTemperature°C"
    }

    fun getmWeatherType(): String? {
        return mWeatherType
    }

    companion object {
        fun fromJson(jsonObject: JSONObject): weatherData? {
            return try {
                val weatherD = weatherData()
                weatherD.mcity = jsonObject.getString("name")
                weatherD.mCondition =
                    jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id")
                weatherD.mWeatherType =
                    jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
                weatherD.micon = updateWeatherIcon(weatherD.mCondition)
                val tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15
                val roundedValue = Math.rint(tempResult).toInt()
                weatherD.mTemperature = Integer.toString(roundedValue)
                weatherD
            } catch (e: JSONException) {
                e.printStackTrace()
                null
            }
        }

        private fun updateWeatherIcon(condition: Int): String {
            if (condition >= 0 && condition <= 300) {
                return "storm"
            } else if (condition >= 300 && condition <= 500) {
                return "lightrain"
            } else if (condition >= 500 && condition <= 600) {
                return "raining"
            } else if (condition >= 600 && condition <= 700) {
                return "snowing"
            } else if (condition >= 701 && condition <= 771) {
                return "fog"
            } else if (condition >= 772 && condition <= 800) {
                return "overcast"
            } else if (condition == 800) {
                return "sunny"
            } else if (condition >= 801 && condition <= 804) {
                return "cloudylogo"
            } else if (condition == 904) {
                return "sunny"
            }
            return "dunno"
        }
    }
}