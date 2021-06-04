package com.adonmo.killerbee.adapter

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ExecutionHelper
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage


class ClientCallback(
    private val mqttEventsHandler: Handler?,
    private val actionCallback: IMQTTConnectionCallback,
    private val connectOptions: ConnectOptions
) : MqttCallbackExtended {
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
                message?.payload
            )
        }
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {}

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
        if(reconnect) {
            ExecutionHelper.executeCallback(mqttEventsHandler) {
                actionCallback.connectActionFinished(
                    MQTTActionStatus.SUCCESS,
                    connectOptions
                )
            }
        }
    }


}