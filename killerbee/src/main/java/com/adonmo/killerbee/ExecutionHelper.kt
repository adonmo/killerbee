package com.adonmo.killerbee

import android.os.Handler

class ExecutionHelper {
    companion object {
        fun executeCallback(mqttEventsHandler: Handler?, callbackMethod: () -> Unit) {
            if (mqttEventsHandler != null) {
                mqttEventsHandler.post { callbackMethod() }
            } else {
                callbackMethod()
            }
        }
    }
}