package com.adonmo.sample.killerbee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import com.adonmo.killerbee.AndroidMQTTClient

class MainActivity : AppCompatActivity() {
    private lateinit var mqttThread: HandlerThread
    private lateinit var mqttHandler: Handler

    private lateinit var mqttClient: AndroidMQTTClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttThread = HandlerThread("mqttThread")
        mqttThread.start()
        mqttHandler = Handler(mqttThread.looper)

        mqttClient = AndroidMQTTClient(this.applicationContext, "OG", mqttHandler)
        mqttClient.connect()
    }
}