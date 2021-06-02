package com.adonmo.killerbee.adapter

import android.os.Handler
import android.util.Log
import com.adonmo.killerbee.Constants.Companion.LOG_TAG
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class Client(private val connectOptions: ConnectOptions, private val mqttEventsHandler: Handler) {
    private val mqttClient: MqttAsyncClient =
        MqttAsyncClient(connectOptions.serverURI, connectOptions.clientID, MemoryPersistence())

    fun connect() {
        try {
            val mqttConnectOptions = MqttConnectOptions()
            connectOptions.username?.let {
                mqttConnectOptions.userName = connectOptions.username
            }
            connectOptions.password?.let {
                mqttConnectOptions.password = connectOptions.password.toCharArray()
            }
            mqttConnectOptions.isCleanSession = true
            Log.d(LOG_TAG, "Connecting to mqtt broker [${connectOptions.serverURI}]")
            mqttClient.connect(mqttConnectOptions)
            Log.d(
                LOG_TAG,
                "Connected to mqtt broker [${connectOptions.serverURI}] for [${connectOptions.clientID}]"
            )
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while connecting for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }

    fun disconnect() {
        try {
            mqttClient.disconnect()
        } catch (me: MqttException) {
            Log.e(
                LOG_TAG,
                "MQTT exception while disconnecting for [${connectOptions.clientID}]",
                me
            )
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }

    fun forceDisconnect() {
        try {
            mqttClient.disconnectForcibly()
        } catch (me: MqttException) {
            Log.e(
                LOG_TAG,
                "MQTT exception while disconnecting for [${connectOptions.clientID}]",
                me
            )
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }

    fun publish(topic: String, payload: ByteArray, qos: Int, retained: Boolean) {
        try {
            mqttClient.publish(topic, payload, qos, retained)
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while publishing for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }

    fun subscribe(topicFilter: String, qos: Int, messageListener: IMqttMessageListener) {
        try {
            mqttClient.subscribe(topicFilter, qos, messageListener)
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while publishing for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }
}