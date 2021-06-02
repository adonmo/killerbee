package com.adonmo.killerbee.adapter

import android.util.Log
import com.adonmo.killerbee.action.MQTTActionContext
import com.adonmo.killerbee.action.MQTTAction
import com.adonmo.killerbee.helper.Constants.Companion.LOG_TAG
import com.adonmo.killerbee.action.MQTTActionListener
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.TimerPingSender
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService

class Client(
    private val connectOptions: ConnectOptions,
    clientCallback: ClientCallback,
    private val mqttActionListener: MQTTActionListener,
    executorService: ScheduledExecutorService?
) {
    private val mqttClient: MqttAsyncClient =
        MqttAsyncClient(connectOptions.serverURI, connectOptions.clientID, MemoryPersistence(), TimerPingSender(), executorService)

    init {
        mqttClient.setCallback(clientCallback)
    }

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
            mqttClient.connect(
                mqttConnectOptions,
                MQTTActionContext(action = MQTTAction.CONNECT, connectOptions),
                mqttActionListener
            )
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
            mqttClient.disconnect(
                MQTTActionContext(action = MQTTAction.DISCONNECT),
                mqttActionListener
            )
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
            mqttClient.publish(
                topic,
                payload,
                qos,
                retained,
                MQTTActionContext(
                    action = MQTTAction.PUBLISH,
                    messagePayload = payload
                ),
                mqttActionListener
            )
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while publishing for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }

    fun subscribe(topicFilter: String, qos: Int) {
        try {
            mqttClient.subscribe(
                topicFilter,
                qos,
                MQTTActionContext(action = MQTTAction.SUBSCRIBE, topic = topicFilter),
                mqttActionListener
            )
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while subscribing for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }

    fun subscribeMultiple(topicFilters: Array<String>, qos: IntArray) {
        try {
            mqttClient.subscribe(
                topicFilters,
                qos,
                MQTTActionContext(
                    action = MQTTAction.SUBSCRIBE_MULTIPLE,
                    topics = topicFilters
                ),
                mqttActionListener
            )
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while subscribing for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }
}