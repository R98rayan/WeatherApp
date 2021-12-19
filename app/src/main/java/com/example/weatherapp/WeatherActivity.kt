package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity() {

    lateinit var nameLabel: TextView
    lateinit var updateLabel: TextView
    lateinit var descriptionLabel: TextView
    lateinit var tempLabel: TextView
    lateinit var lowLabel: TextView
    lateinit var highLabel: TextView

    lateinit var refreshLayout: LinearLayout

    lateinit var sunriseLabel: TextView
    lateinit var sunsetLabel: TextView
    lateinit var windLabel: TextView
    lateinit var pressureLabel: TextView
    lateinit var humidityLabel: TextView

    val APIKey = "38fa66a1977c6cf483a3cd00febfae78"

    fun getWeatherURL(city: String, state: String): String {
        return "https://api.openweathermap.org/data/2.5/weather?q=$city,$state&APPID=$APIKey&units=metric"
    }
    fun getWeatherURL(city: String): String {
        return "https://api.openweathermap.org/data/2.5/weather?q=$city&APPID=$APIKey&units=metric"
    }
    fun getWeatherURL(id: Int): String {
        return "https://api.openweathermap.org/data/2.5/weather?id=$id&APPID=$APIKey&units=metric"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        nameLabel = findViewById(R.id.name)
        updateLabel = findViewById(R.id.updated)
        descriptionLabel = findViewById(R.id.description)
        tempLabel = findViewById(R.id.temp)
        lowLabel = findViewById(R.id.low)
        highLabel = findViewById(R.id.high)

        refreshLayout = findViewById(R.id.refreshLayout)

        sunriseLabel = findViewById(R.id.sunriseText)
        sunsetLabel = findViewById(R.id.sunsetText)
        windLabel = findViewById(R.id.windText)
        pressureLabel = findViewById(R.id.pressureText)
        humidityLabel = findViewById(R.id.humidityText)

        updateData()

        refreshLayout.setOnClickListener() {
            updateData()
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
        }

        tempLabel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    fun updateData() {
        var name = ""
        var country = ""
        var description = ""
        var temp = 0.0
        var low = 0.0
        var high = 0.0

        var sunrise = 0
        var sunset = 0
        var wind = 0.0
        var pressure = 0
        var humidity = 0

        CoroutineScope(Dispatchers.IO).launch {

            var data = async { fetchData() }.await()

            if(!data.isEmpty()) {

                var jsonOfWeather = JSONObject(data)
                name = jsonOfWeather.getString("name")
                country = jsonOfWeather.getJSONObject("sys").getString("country")

                var t = jsonOfWeather.getJSONArray("weather").get(0) as JSONObject
                description = t.getString("description")

                temp = jsonOfWeather.getJSONObject("main").getDouble("temp")
                low = jsonOfWeather.getJSONObject("main").getDouble("temp_min")
                high = jsonOfWeather.getJSONObject("main").getDouble("temp_max")

                sunrise = jsonOfWeather.getJSONObject("sys").getInt("sunrise")
                sunset = jsonOfWeather.getJSONObject("sys").getInt("sunset")

                wind = jsonOfWeather.getJSONObject("wind").getDouble("speed")

                pressure = jsonOfWeather.getJSONObject("main").getInt("pressure")
                humidity = jsonOfWeather.getJSONObject("main").getInt("humidity")

                withContext(Dispatchers.Main){
                    nameLabel.text = "$name, $country"
                    updateLabel.text = "updated at: "+ SimpleDateFormat("DD/MM/YYYY hh.mm aa").format(
                        Date()
                    )
                    descriptionLabel.text = description
                    tempLabel.text = temp.roundToInt().toString()+"°C"
                    lowLabel.text = "Low: "+low.roundToInt().toString()+"°C"
                    highLabel.text = "High: "+high.roundToInt().toString()+"°C"

                    sunriseLabel.text = SimpleDateFormat("hh.mm aa").format(Date(sunrise.toLong()))
                    sunsetLabel.text = SimpleDateFormat("hh.mm aa").format(Date(sunset.toLong()))
                    windLabel.text = wind.toString()
//
                    pressureLabel.text = pressure.toString()
                    humidityLabel.text = humidity.toString()
                    Log.d("MAIN", "UI Updated!")
                }

            }
        }
    }


    fun fetchData(): String{

        var response = ""
        try{
            var tempString: String? = intent.getStringExtra("DataTrasfer")
            response = URL(tempString?.let { getWeatherURL(it) }).readText()
        }catch(e: Exception){
            Log.d("MAIN", "ISSUE: $e")
        }
        // our response is saved as a string and returned
        return response
    }
}