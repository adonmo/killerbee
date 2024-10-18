package com.adonmo.killerbee.adapter

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ExecutionHelper

import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties


class ClientCallback(
    private val mqttEventsHandler: Handler?,
    private val actionCallback: IMQTTConnectionCallback,
    private val connectOptions: ConnectOptions
) : MqttCallback {

    override fun disconnected(disconnectResponse: MqttDisconnectResponse?) {
        ExecutionHelper.executeCallback(mqttEventsHandler){
            actionCallback.disconnected(connectOptions, disconnectResponse)
        }
    }
    override fun mqttErrorOccurred(exception: MqttException?) {}

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        ExecutionHelper.executeCallback(mqttEventsHandler) {
            actionCallback.messageArrived(
                topic,
                message?.payload
            )
        }
    }

    override fun deliveryComplete(token: IMqttToken?) {}

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
        if (reconnect) {
            ExecutionHelper.executeCallback(mqttEventsHandler) {
                actionCallback.connectActionFinished(
                    MQTTActionStatus.SUCCESS,
                    connectOptions
                )
            }
        }
    }

    override fun authPacketArrived(reasonCode: Int, properties: MqttProperties?) {}

}