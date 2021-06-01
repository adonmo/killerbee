package com.adonmo.killerbee

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import com.adonmo.killerbee.Constants.Companion.LOG_TAG
import com.adonmo.killerbee.adapter.Client
import com.adonmo.killerbee.rpc.request.ConnectOptions
import com.adonmo.killerbee.rpc.request.Helper.Companion.getMessageForRPC
import com.adonmo.killerbee.rpc.request.Request
import com.adonmo.killerbee.rpc.request.RequestCode
import com.adonmo.killerbee.service.AndroidMQTTService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


class AndroidMQTTClient(
    private val appContext: Context,
    private val connectOptions: ConnectOptions,
    private val mqttEventsHandler: Handler,
    private val mqttNetworkCallHandler: Handler
) {

    private val mqttClientAdapter: Client = Client(connectOptions, mqttEventsHandler)

    private fun connect() {
        Log.d(LOG_TAG, "Sending connect action for [${connectOptions.clientID}]")
        mqttNetworkCallHandler.post { mqttClientAdapter.connect() }
        Log.d(LOG_TAG, "Triggered connect action for [${connectOptions.clientID}]")
    }

    private fun disconnect() {
        Log.d(LOG_TAG, "Sending disconnect action for [${connectOptions.clientID}]")
        mqttNetworkCallHandler.post{}
        Log.d(LOG_TAG, "Triggered connect action for [${connectOptions.clientID}]")
    }

}