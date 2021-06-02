package com.adonmo.killerbee

import com.adonmo.killerbee.adapter.ConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

interface AndroidMqttActionCallback {

    fun connectActionFinished(status: AndroidMqttActionStatus, connectOptions: ConnectOptions)

    fun disconnectActionFinished(status: AndroidMqttActionStatus)

    fun publishActionFinished(status: AndroidMqttActionStatus, messagePayload: ByteArray)

    fun subscribeActionFinished(status: AndroidMqttActionStatus, topic: String)

    fun subscribeMultipleActionFinished(status: AndroidMqttActionStatus, topics: Array<String>)

    fun connectionLost(connectOptions: ConnectOptions)

    fun messageArrived(topic: String?, message: MqttMessage?)

}