package com.adonmo.sample.killerbee

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.adonmo.killerbee.AndroidMQTTClient
import com.adonmo.killerbee.Constants
import com.adonmo.killerbee.adapter.ConnectOptions

class MainActivity : AppCompatActivity() {
    private lateinit var mqttThread: HandlerThread
    private lateinit var mqttHandler: Handler

    private lateinit var mqttClient: AndroidMQTTClient

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(Constants.LOG_TAG, "Running on thread [${Thread.currentThread()}]")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttThread = HandlerThread("mqttThread")
        mqttThread.start()
        mqttHandler = Handler(mqttThread.looper)

        mqttClient = AndroidMQTTClient(
            ConnectOptions(clientID = "OG", serverURI = "tcp://broker.hivemq.com:1883"),
            mqttHandler
        )
    }
}