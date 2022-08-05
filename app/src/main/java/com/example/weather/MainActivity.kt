package com.example.weather

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.location.LocationManager
import android.widget.TextView
import android.widget.RelativeLayout
import android.location.LocationListener
import android.os.Bundle
import com.example.weather.R
import android.content.Intent
import com.example.weather.cityFinder
import com.loopj.android.http.RequestParams
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import org.json.JSONObject
import com.example.weather.weatherData
import cz.msebera.android.httpclient.Header

class MainActivity : AppCompatActivity() {
    val APP_ID = "dab3af44de7d24ae7ff86549334e45bd"
    val WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"
    val MIN_TIME: Long = 5000
    val MIN_DISTANCE = 1000f
    val REQUEST_CODE = 101
    var Location_Provider = LocationManager.GPS_PROVIDER
    var NameofCity: TextView? = null
    var weatherState: TextView? = null
    var Temperature: TextView? = null
    var mweatherIcon: ImageView? = null
    var mLocationManager: LocationManager? = null
    var mLocationListner: LocationListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherState = findViewById(R.id.weatherCondition)
        Temperature = findViewById(R.id.temperature)
        mweatherIcon = findViewById(R.id.weatherIcon)
        var mCityFinder: RelativeLayout = findViewById(R.id.cityFinder)
        NameofCity = findViewById(R.id.cityName)
        mCityFinder.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, cityFinder::class.java)
            startActivity(intent)
        })
    }

    /*   @Override
   protected void onResume() {
       super.onResume();
       getWeatherForCurrentLocation();
    }*/
    override fun onResume() {
        super.onResume()
        val mIntent = intent
        val city = mIntent.getStringExtra("City")
        if (city != null) {
            getWeatherForNewCity(city)
        } else {
            weatherForCurrentLocation
        }
    }

    private fun getWeatherForNewCity(city: String) {
        val params = RequestParams()
        params.put("q", city)
        params.put("appid", APP_ID)
        letsdoSomeNetworking(params)
    }// TODO: Consider calling

    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    //not able to get location
    private val weatherForCurrentLocation: Unit
        private get() {
            mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            mLocationListner = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    val Latitude = location.latitude.toString()
                    val Longitude = location.longitude.toString()
                    val params = RequestParams()
                    params.put("lat", Latitude)
                    params.put("lon", Longitude)
                    params.put("appid", APP_ID)
                    letsdoSomeNetworking(params)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {
                    //not able to get location
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
                return
            }
            mLocationManager!!.requestLocationUpdates(
                Location_Provider,
                MIN_TIME,
                MIN_DISTANCE,
                mLocationListner as LocationListener
            )
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Locationget Succesffully", Toast.LENGTH_SHORT)
                    .show()
                weatherForCurrentLocation
            } else {
                //user denied the permission
            }
        }
    }

    private fun letsdoSomeNetworking(params: RequestParams) {
        val client = AsyncHttpClient()
        client[WEATHER_URL, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject) {
                Toast.makeText(this@MainActivity, "Data Get Success", Toast.LENGTH_SHORT).show()
                val weatherD = weatherData.fromJson(response)
                if (weatherD != null) {
                    updateUI(weatherD)
                }


                // super.onSuccess(statusCode, headers, response);
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                throwable: Throwable,
                errorResponse: JSONObject
            ) {
                //super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        }]
    }

    private fun updateUI(weather: weatherData) {
        Temperature!!.text = weather.getmTemperature()
        NameofCity!!.text = weather.mcity
        weatherState!!.text = weather.getmWeatherType()
        val resourceID = resources.getIdentifier(weather.micon, "drawable", packageName)
        mweatherIcon!!.setImageResource(resourceID)
    }


}