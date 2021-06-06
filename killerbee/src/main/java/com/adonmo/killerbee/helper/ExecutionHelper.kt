package com.adonmo.killerbee.helper

import android.os.Handler
import org.eclipse.paho.client.mqttv3.MqttException

object ExecutionHelper {
    fun executeCallback(mqttEventsHandler: Handler?, callbackMethod: () -> Unit) {
        if (mqttEventsHandler != null) {
            mqttEventsHandler.post { callbackMethod() }
        } else {
            callbackMethod()
        }
    }

    fun executeMQTTClientAction(
        actionMethod: () -> Unit,
        errorCallbackMethod: (e: Exception) -> Unit,
        mqttEventsHandler: Handler? = null
    ) {
        try {
            actionMethod()
        } catch (me: MqttException) {
            executeCallback(mqttEventsHandler) { errorCallbackMethod(me) }
        } catch (e: Exception) {
            executeCallback(mqttEventsHandler) { errorCallbackMethod(e) }
        }
    }
}
