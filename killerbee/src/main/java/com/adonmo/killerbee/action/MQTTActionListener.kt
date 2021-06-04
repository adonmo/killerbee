package com.adonmo.killerbee.action

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.helper.ExecutionHelper
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken

class MQTTActionListener(
    private val mqttEventsHandler: Handler?,
    private val mqttConnectionCallback: IMQTTConnectionCallback
) : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        executeUserActionCallback(asyncActionToken, MQTTActionStatus.SUCCESS)
    }

    override fun onFailure(asyncActionToken: IMqttToken?, throwable: Throwable?) {
        executeUserActionCallback(asyncActionToken, MQTTActionStatus.FAILED, Exception(throwable?.localizedMessage))
    }

    private fun executeUserActionCallback(asyncActionToken: IMqttToken?, status: MQTTActionStatus, exception: Exception? = null) {
        ExecutionHelper.executeCallback(mqttEventsHandler) {
            val context = asyncActionToken?.userContext as MQTTActionContext
            when (context.action) {
                MQTTAction.CONNECT ->
                    mqttConnectionCallback.connectActionFinished(
                        status,
                        context.connectOptions!!,
                        exception
                    )
                MQTTAction.DISCONNECT ->
                    mqttConnectionCallback.disconnectActionFinished(status, exception)
                MQTTAction.PUBLISH ->
                    mqttConnectionCallback.publishActionFinished(
                        status,
                        context.messagePayload!!,
                        exception
                    )
                MQTTAction.SUBSCRIBE ->
                    mqttConnectionCallback.subscribeActionFinished(
                        status,
                        context.topic!!,
                        exception
                    )
                MQTTAction.SUBSCRIBE_MULTIPLE ->
                    mqttConnectionCallback.subscribeMultipleActionFinished(
                        status,
                        context.topics!!,
                        exception
                    )
            }
        }
    }

}