package com.adonmo.sample.killerbee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import com.adonmo.killerbee.AndroidMQTTClient

class MainActivity : AppCompatActivity() {
    private lateinit var mqttThread: HandlerThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttThread = HandlerThread("mqttThread")
        mqttThread.start()
        val c = AndroidMQTTClient(mqttRunnerThread = mqttThread)
        c.runMQTTClient()
    }
}