package com.adonmo.killerbee

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


class AndroidMQTTClient(private val mqttRunnerThread: HandlerThread) {

    private lateinit var mqttHandler: Handler

    fun runMQTTClient() {
        mqttHandler = Handler(mqttRunnerThread.looper)
        mqttHandler.post {
            val topic = "MQTT Examples"
            val content = "Message from MqttPublishSample"
            val qos = 2
            val broker = "tcp://broker.hivemq.com:1883"
            val clientId = "JavaSample"
            val persistence = MemoryPersistence()
            try {
                val sampleClient = MqttClient(broker, clientId, persistence)
                val connOpts = MqttConnectOptions()
                connOpts.isCleanSession = true
                Log.d(LOG_TAG,"Connecting to broker: $broker")
                sampleClient.connect(connOpts)
                Log.d(LOG_TAG,"Connected")
                Log.d(LOG_TAG,"Publishing message: $content")
                val message = MqttMessage(content.toByteArray())
                message.qos = qos
                sampleClient.publish(topic, message)
                Log.d(LOG_TAG,"Message published")
                sampleClient.disconnect()
                Log.d(LOG_TAG,"Disconnected")
            } catch (me: MqttException) {
                Log.d(LOG_TAG,"reason " + me.reasonCode)
                Log.d(LOG_TAG,"msg " + me.message)
                Log.d(LOG_TAG,"loc " + me.localizedMessage)
                Log.d(LOG_TAG,"cause " + me.cause)
                Log.e(LOG_TAG,"exception $me", me)
            }
        }
    }

    companion object {
        const val LOG_TAG = "mqttClient"
    }

}