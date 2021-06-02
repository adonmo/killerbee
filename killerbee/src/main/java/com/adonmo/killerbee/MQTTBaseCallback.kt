package com.adonmo.killerbee

import android.os.Handler
import com.adonmo.killerbee.adapter.ConnectOptions
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage


class MQTTBaseCallback(
    private val mqttEventsHandler: Handler?,
    private val actionCallback: AndroidMqttActionCallback,
    private val connectOptions: ConnectOptions
) : MqttCallback {
    override fun connectionLost(cause: Throwable?) {
        ExecutionHelper.executeCallback(mqttEventsHandler) {
            actionCallback.connectionLost(
                connectOptions
            )
        }
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        ExecutionHelper.executeCallback(mqttEventsHandler) {
            actionCallback.messageArrived(
                topic,
                message
            )
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {}


}