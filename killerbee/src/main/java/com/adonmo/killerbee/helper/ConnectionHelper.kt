package com.adonmo.killerbee.helper

import com.adonmo.killerbee.adapter.ConnectOptions
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.TimerPingSender
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.concurrent.ScheduledExecutorService

object ConnectionHelper {
    fun getMqttConnectOptions(connectOptions: ConnectOptions): MqttConnectOptions {
        val mqttConnectOptions = MqttConnectOptions()
        connectOptions.customWebSocketHeaders?.let {
            mqttConnectOptions.customWebSocketHeaders = it
        }
        connectOptions.serverURIs?.let {
            mqttConnectOptions.serverURIs = it
        }
        connectOptions.sslHostnameVerifier?.let {
            mqttConnectOptions.sslHostnameVerifier = it
        }
        connectOptions.socketFactory?.let {
            mqttConnectOptions.socketFactory = it
        }
        connectOptions.sslClientProps?.let {
            mqttConnectOptions.sslProperties = it
        }
        connectOptions.willDestination?.let { dest ->
            connectOptions.willMessagePayload?.let { payload ->
                mqttConnectOptions.setWill(
                    dest,
                    payload,
                    connectOptions.willQos,
                    connectOptions.willRetained
                )
            }
        }
        connectOptions.username?.let {
            mqttConnectOptions.userName = connectOptions.username
        }
        connectOptions.password?.let {
            mqttConnectOptions.password = connectOptions.password.toCharArray()
        }
        mqttConnectOptions.isAutomaticReconnect = connectOptions.automaticReconnect
        mqttConnectOptions.isCleanSession = connectOptions.cleanSession
        mqttConnectOptions.maxReconnectDelay = connectOptions.maxReconnectDelay
        mqttConnectOptions.keepAliveInterval = connectOptions.keepAliveInterval
        mqttConnectOptions.connectionTimeout = connectOptions.connectionTimeout

        mqttConnectOptions.executorServiceTimeout = connectOptions.executorServiceTimeout
        mqttConnectOptions.isHttpsHostnameVerificationEnabled =
            connectOptions.httpsHostnameVerificationEnabled
        mqttConnectOptions.maxInflight = connectOptions.maxInFlight
        mqttConnectOptions.mqttVersion = connectOptions.mqttVersion
        return mqttConnectOptions
    }

    fun getMqttAsyncClient(
        connectOptions: ConnectOptions,
        executorService: ScheduledExecutorService?
    ): MqttAsyncClient {
        return MqttAsyncClient(
            connectOptions.serverURI,
            connectOptions.clientID,
            MemoryPersistence(),
            TimerPingSender(),
            executorService
        )
    }
}