package com.adonmo.killerbee.service

import android.os.Handler
import android.os.Message
import android.os.Messenger
import android.util.Log
import com.adonmo.killerbee.Constants.Companion.LOG_TAG
import com.adonmo.killerbee.adapter.Client
import com.adonmo.killerbee.rpc.request.ConnectOptions
import com.adonmo.killerbee.rpc.request.Helper
import com.adonmo.killerbee.rpc.request.RequestCode
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class RPCHandler : Handler() {
    private val clientIDToClient: MutableMap<String, Client> = mutableMapOf()

    override fun handleMessage(msg: Message) {
        val request = Helper.getRPCRequestFromMessage(msg)
        Log.v(LOG_TAG, "Received rpc request [$request]")
        when (request?.code) {
            RequestCode.CONNECT -> {
                val connectOptions = (request.payload) as ConnectOptions
                connect(connectOptions, msg.replyTo)
            }
            else -> {
                Log.d(LOG_TAG, "Bad request code")
            }
        }
        super.handleMessage(msg)
    }

    private fun connect(connectOptions: ConnectOptions, replyTo: Messenger) {
        val persistence = MemoryPersistence()
        try {
            val mqttClient =
                MqttClient(connectOptions.serverURI, connectOptions.clientID, persistence)
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
            clientIDToClient[connectOptions.clientID] = Client(mqttClient, replyTo)
        } catch (me: MqttException) {
            Log.e(LOG_TAG, "MQTT exception while connecting for [${connectOptions.clientID}]", me)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected error", e)
        }
    }
}