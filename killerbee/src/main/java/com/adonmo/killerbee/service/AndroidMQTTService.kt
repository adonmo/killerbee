package com.adonmo.killerbee.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.eclipse.paho.client.mqttv3.MqttMessage

class AndroidMQTTService: Service() {
    val m: MqttMessage = MqttMessage()

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}