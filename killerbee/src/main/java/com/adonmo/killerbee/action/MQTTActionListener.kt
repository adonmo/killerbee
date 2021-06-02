package com.adonmo.killerbee.action

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.helper.ExecutionHelper
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken

class MQTTActionListener(
    private val mqttEventsHandler: Handler?,
    private val IMQTTConnectionCallback: IMQTTConnectionCallback
) : IMqttActionListener {
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        executeUserActionCallback(asyncActionToken, MQTTActionStatus.SUCCESS)
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        executeUserActionCallback(asyncActionToken, MQTTActionStatus.FAILED)
    }

    private fun executeUserActionCallback(asyncActionToken: IMqttToken?, status: MQTTActionStatus) {
        ExecutionHelper.executeCallback(mqttEventsHandler) {
            val context = asyncActionToken?.userContext as MQTTActionContext
            when (context.action) {
                MQTTAction.CONNECT ->
                    IMQTTConnectionCallback.connectActionFinished(
                        status,
                        context.connectOptions!!
                    )
                MQTTAction.DISCONNECT ->
                    IMQTTConnectionCallback.disconnectActionFinished(status)
                MQTTAction.PUBLISH ->
                    IMQTTConnectionCallback.publishActionFinished(
                        status,
                        context.messagePayload!!
                    )
                MQTTAction.SUBSCRIBE ->
                    IMQTTConnectionCallback.subscribeActionFinished(
                        status,
                        context.topic!!
                    )
                MQTTAction.SUBSCRIBE_MULTIPLE ->
                    IMQTTConnectionCallback.subscribeMultipleActionFinished(
                        status,
                        context.topics!!
                    )
            }
        }
    }

}