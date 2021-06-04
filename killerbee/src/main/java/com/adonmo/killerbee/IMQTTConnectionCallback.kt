package com.adonmo.killerbee

import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.adapter.ConnectOptions

interface IMQTTConnectionCallback {

    fun connectActionFinished(
        status: MQTTActionStatus,
        connectOptions: ConnectOptions,
        exception: Exception? = null
    )

    fun disconnectActionFinished(status: MQTTActionStatus, exception: Exception? = null)

    fun publishActionFinished(
        status: MQTTActionStatus,
        messagePayload: ByteArray,
        exception: Exception? = null
    )

    fun subscribeActionFinished(
        status: MQTTActionStatus,
        topic: String,
        exception: Exception? = null
    )

    fun subscribeMultipleActionFinished(
        status: MQTTActionStatus,
        topics: Array<String>,
        exception: Exception? = null
    )

    fun connectionLost(connectOptions: ConnectOptions)

    fun messageArrived(topic: String?, message: ByteArray?)

}