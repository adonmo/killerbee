package com.adonmo.killerbee

import android.os.Handler
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken

class MQTTActionListener(
    private val mqttEventsHandler: Handler?,
    private val androidMqttActionCallback: AndroidMqttActionCallback
) : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        executeUserActionCallback(asyncActionToken, AndroidMqttActionStatus.SUCCESS)
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        executeUserActionCallback(asyncActionToken, AndroidMqttActionStatus.FAILED)
    }

    fun executeUserActionCallback(asyncActionToken: IMqttToken?, status: AndroidMqttActionStatus) {
        ExecutionHelper.executeCallback(mqttEventsHandler) {
            val context = asyncActionToken?.userContext as AndroidMQTTUserContext
            when (context.action) {
                AndroidMqttAction.CONNECT ->
                    androidMqttActionCallback.connectActionFinished(
                        status,
                        context.connectOptions!!
                    )
                AndroidMqttAction.DISCONNECT ->
                    androidMqttActionCallback.disconnectActionFinished(status)
                AndroidMqttAction.PUBLISH ->
                    androidMqttActionCallback.publishActionFinished(
                        status,
                        context.messagePayload!!
                    )
                AndroidMqttAction.SUBSCRIBE ->
                    androidMqttActionCallback.subscribeActionFinished(
                        status,
                        context.topic!!
                    )
                AndroidMqttAction.SUBSCRIBE_MULTIPLE ->
                    androidMqttActionCallback.subscribeMultipleActionFinished(
                        status,
                        context.topics!!
                    )
            }
        }
    }

}