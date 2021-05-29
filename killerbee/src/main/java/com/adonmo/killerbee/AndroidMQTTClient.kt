package com.adonmo.killerbee

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.adonmo.killerbee.Constants.Companion.LOG_TAG
import com.adonmo.killerbee.service.AndroidMQTTService


class AndroidMQTTClient(private val appContext: Context, private val clientID: String, private val mqttEventsHandler: Handler) {

    private val mqttServiceConnection: ServiceConnection

    init {
        val mqttClientThis = this
        mqttServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
                Log.d(LOG_TAG, "Connected to underlying service for [$clientID]")
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                Log.d(LOG_TAG, "Disconnected from underlying service for [$clientID]")
                mqttEventsHandler.postDelayed(mqttClientThis::initServiceConnection, 2000);
            }

        }
    }

    fun connect() {
        initServiceConnection()
    }

    private fun initServiceConnection() {
        Log.d(LOG_TAG, "Initiating service connection for client [$clientID]")
        try {
            val intent = Intent()
            intent.setClass(appContext, AndroidMQTTService::class.java);
            appContext.bindService(intent, mqttServiceConnection, Service.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unable to bind to underlying service for client [$clientID]")
            mqttEventsHandler.postDelayed(this::initServiceConnection, 2000)
        }
    }
}