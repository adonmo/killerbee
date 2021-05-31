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
import com.adonmo.killerbee.rpc.request.ConnectOptions
import com.adonmo.killerbee.rpc.request.Helper.Companion.getMessageForRPC
import com.adonmo.killerbee.rpc.request.Request
import com.adonmo.killerbee.rpc.request.RequestCode
import com.adonmo.killerbee.service.AndroidMQTTService


class AndroidMQTTClient(
    private val appContext: Context,
    private val connectOptions: ConnectOptions,
    private val mqttEventsHandler: Handler
) {

    private val mqttServiceConnection: ServiceConnection
    private var mqttServiceSendChannel: Messenger? = null
    private val mqttClientReceiveChannel: Messenger

    init {
        val mqttClientThis = this
        mqttServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
                Log.d(LOG_TAG, "Connected to underlying service for [${connectOptions.clientID}]")
                mqttServiceSendChannel = Messenger(binder)
                connect()
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                Log.d(
                    LOG_TAG,
                    "Disconnected from underlying service for [${connectOptions.clientID}]"
                )
                mqttEventsHandler.postDelayed(mqttClientThis::initServiceConnection, 2000)
                mqttServiceSendChannel = null
            }

        }
        mqttClientReceiveChannel = Messenger(mqttEventsHandler)
        mqttEventsHandler.postDelayed(this::initServiceConnection, 2000)
    }

    private fun connect() {
        Log.d(LOG_TAG, "Sending connect action for [${connectOptions.clientID}]")
        val message =
            getMessageForRPC(Request(RequestCode.CONNECT, connectOptions), mqttClientReceiveChannel)
        mqttServiceSendChannel?.send(message)
        Log.d(LOG_TAG, "Triggered connect action for [${connectOptions.clientID}]")
    }

    private fun initServiceConnection() {
        Log.d(LOG_TAG, "Initiating service connection for client [${connectOptions.clientID}]")
        try {
            val intent = Intent()
            intent.setClass(appContext, AndroidMQTTService::class.java)
            appContext.bindService(intent, mqttServiceConnection, Service.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            Log.e(
                LOG_TAG,
                "Unable to bind to underlying service for client [${connectOptions.clientID}]"
            )

        }
    }

}