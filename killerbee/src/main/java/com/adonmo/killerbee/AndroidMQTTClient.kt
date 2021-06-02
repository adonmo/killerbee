package com.adonmo.killerbee

import android.os.Handler
import android.util.Log
import com.adonmo.killerbee.action.MQTTActionListener
import com.adonmo.killerbee.adapter.Client
import com.adonmo.killerbee.adapter.ClientCallback
import com.adonmo.killerbee.adapter.ConnectOptions
import com.adonmo.killerbee.helper.Constants.Companion.LOG_TAG
import java.util.concurrent.ScheduledExecutorService


class AndroidMQTTClient(
    private val connectOptions: ConnectOptions,
    mqttEventsHandler: Handler,
    androidMqttActionCallback: IMQTTConnectionCallback,
    executorService: ScheduledExecutorService?
) {

    private val mqttClientAdapter = Client(
        connectOptions,
        ClientCallback(mqttEventsHandler, androidMqttActionCallback, connectOptions),
        MQTTActionListener(
            mqttEventsHandler,
            androidMqttActionCallback
        ),
        executorService
    )

    fun connect() {
        Log.d(LOG_TAG, "Sending connect action for [${connectOptions.clientID}]")
        mqttClientAdapter.connect()
        Log.d(LOG_TAG, "Triggered connect action for [${connectOptions.clientID}]")
    }

    fun disconnect() {
        Log.d(LOG_TAG, "Sending disconnect action for [${connectOptions.clientID}]")
        mqttClientAdapter.disconnect()
        Log.d(LOG_TAG, "Triggered connect action for [${connectOptions.clientID}]")
    }

    fun forceDisconnect() {
        Log.d(LOG_TAG, "Sending forced disconnect action for [${connectOptions.clientID}]")
        mqttClientAdapter.forceDisconnect()
        Log.d(LOG_TAG, "Triggered forced disconnect action for [${connectOptions.clientID}]")
    }

    fun publish(topic: String, payload: ByteArray, qos: Int, retained: Boolean) {
        Log.v(
            LOG_TAG,
            "Sending publish for [${connectOptions.clientID}] on [$topic] for [${payload.size}] bytes"
        )
        mqttClientAdapter.publish(topic, payload, qos, retained)
        Log.v(
            LOG_TAG,
            "Triggered publish for [${connectOptions.clientID}] on [$topic] for [${payload.size}] bytes"
        )
    }

    fun subscribe(topic: String, qos: Int) {
        Log.d(LOG_TAG, "Sending subscribe for [${connectOptions.clientID}] on [$topic]")
        mqttClientAdapter.subscribe(topic, qos)
        Log.d(LOG_TAG, "Triggered subscribe for [${connectOptions.clientID}] on [$topic]")
    }

    fun subscribe(topics: Array<String>, qos: IntArray) {
        Log.d(LOG_TAG, "Sending subscribe for [${connectOptions.clientID}] on [$topics]")
        mqttClientAdapter.subscribeMultiple(topics, qos)
        Log.d(LOG_TAG, "Triggered subscribe for [${connectOptions.clientID}] on [$topics]")
    }

}