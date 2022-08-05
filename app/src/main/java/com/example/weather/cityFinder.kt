package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.R
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import android.widget.TextView
import android.content.Intent
import android.widget.ImageView
import com.example.weather.MainActivity

class cityFinder : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_finder)
        val editText = findViewById<EditText>(R.id.searchCity)
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener { finish() }
        editText.setOnEditorActionListener { v, actionId, event ->
            val newCity = editText.text.toString()
            val intent = Intent(this@cityFinder, MainActivity::class.java)
            intent.putExtra("City", newCity)
            startActivity(intent)
            false
        }
    }
}