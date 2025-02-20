package com.adonmo.killerbee

import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.adapter.ConnectOptions
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse

interface IMQTTConnectionCallback {

    fun connectActionFinished(
        status: MQTTActionStatus,
        connectOptions: ConnectOptions,
        throwable: Throwable? = null
    )

    fun disconnectActionFinished(status: MQTTActionStatus, throwable: Throwable? = null)

    fun publishActionFinished(
        status: MQTTActionStatus,
        messagePayload: ByteArray,
        throwable: Throwable? = null
    )

    fun subscribeActionFinished(
        status: MQTTActionStatus,
        topic: String,
        throwable: Throwable? = null
    )

    fun subscribeMultipleActionFinished(
        status: MQTTActionStatus,
        topics: Array<String>,
        throwable: Throwable? = null
    )

    fun messageArrived(topic: String?, message: ByteArray?)

    fun disconnected(connectOptions: ConnectOptions, disconnectResponse: MqttDisconnectResponse? = null)

}