package com.adonmo.killerbee

import android.os.Handler
import android.util.Log
import com.adonmo.killerbee.Constants.Companion.LOG_TAG
import com.adonmo.killerbee.adapter.Client
import com.adonmo.killerbee.adapter.ConnectOptions


class AndroidMQTTClient(
    private val connectOptions: ConnectOptions,
    private val mqttEventsHandler: Handler
) {

    private val mqttClientAdapter = Client(connectOptions, mqttEventsHandler)

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

    fun publish() {

    }

    fun subscribe(topic: String) {

    }

    fun subscribe(topics: Array<String>) {

    }

}