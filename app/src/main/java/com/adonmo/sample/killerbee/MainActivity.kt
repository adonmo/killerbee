package com.adonmo.sample.killerbee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adonmo.killerbee.AndroidMQTTClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val c = AndroidMQTTClient()
    }
}