package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    lateinit var cityLabel: TextView
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityLabel = findViewById(R.id.cityLabel)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
//            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(Intent(this, WeatherActivity::class.java).putExtra("DataTrasfer", "${cityLabel.text}"))
            startActivity(intent)
        }
    }


}