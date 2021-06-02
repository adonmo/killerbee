package com.adonmo.killerbee

import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.adapter.ConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

interface IMQTTConnectionCallback {

    fun connectActionFinished(status: MQTTActionStatus, connectOptions: ConnectOptions)

    fun disconnectActionFinished(status: MQTTActionStatus)

    fun publishActionFinished(status: MQTTActionStatus, messagePayload: ByteArray)

    fun subscribeActionFinished(status: MQTTActionStatus, topic: String)

    fun subscribeMultipleActionFinished(status: MQTTActionStatus, topics: Array<String>)

    fun connectionLost(connectOptions: ConnectOptions)

    fun messageArrived(topic: String?, message: ByteArray?)

}