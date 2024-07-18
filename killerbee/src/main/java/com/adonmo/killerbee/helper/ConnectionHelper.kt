package com.adonmo.killerbee.helper

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTActionListener
import com.adonmo.killerbee.adapter.Client
import com.adonmo.killerbee.adapter.ClientCallback
import com.adonmo.killerbee.adapter.ConnectOptions
import java.util.concurrent.ScheduledExecutorService
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.MqttAsyncClient
import org.eclipse.paho.mqttv5.client.TimerPingSender
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties

object ConnectionHelper {
    fun getMqttConnectOptions(connectOptions: ConnectOptions): MqttConnectionOptions {
        val mqttConnectOptions = MqttConnectionOptions()
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
                val mqttMessage = MqttMessage()
                mqttMessage.payload= payload
                mqttMessage.qos = connectOptions.willQos
                mqttMessage.isRetained=connectOptions.willRetained

                mqttConnectOptions.setWill(
                    dest,
                    mqttMessage
                )
            }
        }
        connectOptions.username?.let {
            mqttConnectOptions.userName = connectOptions.username
        }
        connectOptions.password?.let {
            mqttConnectOptions.password = connectOptions.password.toByteArray()
        }
        mqttConnectOptions.isAutomaticReconnect = connectOptions.automaticReconnect
        mqttConnectOptions.isCleanStart = connectOptions.cleanStart
        mqttConnectOptions.maxReconnectDelay = connectOptions.maxReconnectDelay
        mqttConnectOptions.keepAliveInterval = connectOptions.keepAliveInterval
        mqttConnectOptions.connectionTimeout = connectOptions.connectionTimeout

        mqttConnectOptions.executorServiceTimeout = connectOptions.executorServiceTimeout
        mqttConnectOptions.isHttpsHostnameVerificationEnabled =
            connectOptions.httpsHostnameVerificationEnabled
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
            TimerPingSender(executorService),
            executorService
        )
    }

    fun getMqttClientAdapter(connectOptions: ConnectOptions,
                             mqttEventsHandler: Handler,
                             androidMqttActionCallback: IMQTTConnectionCallback,
                             executorService: ScheduledExecutorService?
    ): Client {
        return  Client(
            connectOptions,
            ClientCallback(mqttEventsHandler, androidMqttActionCallback, connectOptions),
            MQTTActionListener(
                mqttEventsHandler,
                androidMqttActionCallback
            ),
            executorService,
            androidMqttActionCallback,
            mqttEventsHandler
        )
    }
}