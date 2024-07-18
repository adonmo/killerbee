package com.adonmo.killerbee.adapter

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTAction
import com.adonmo.killerbee.action.MQTTActionContext
import com.adonmo.killerbee.action.MQTTActionListener
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ConnectionHelper
import com.adonmo.killerbee.helper.ExecutionHelper
import org.eclipse.paho.mqttv5.client.MqttAsyncClient
import java.util.concurrent.ScheduledExecutorService

class Client(
    private val connectOptions: ConnectOptions,
    clientCallback: ClientCallback,
    private val mqttActionListener: MQTTActionListener,
    executorService: ScheduledExecutorService?,
    private val androidConnectionCallback: IMQTTConnectionCallback,
    private val mqttEventsHandler: Handler? = null
) {
    private val mqttClient: MqttAsyncClient =
        ConnectionHelper.getMqttAsyncClient(connectOptions, executorService)

    init {
        mqttClient.setCallback(clientCallback)
    }

    fun connect() {
        ExecutionHelper.executeMQTTClientAction(
            {
                val mqttConnectOptions = ConnectionHelper.getMqttConnectOptions(connectOptions)
                mqttClient.connect(
                    mqttConnectOptions,
                    MQTTActionContext(action = MQTTAction.CONNECT, connectOptions),
                    mqttActionListener
                )
            },
            { e ->
                androidConnectionCallback.connectActionFinished(
                    MQTTActionStatus.FAILED,
                    connectOptions,
                    e
                )
            }, mqttEventsHandler
        )
    }

    fun disconnect() {
        ExecutionHelper.executeMQTTClientAction(
            {
                mqttClient.disconnect(
                    MQTTActionContext(action = MQTTAction.DISCONNECT),
                    mqttActionListener
                )
            },
            { e -> androidConnectionCallback.disconnectActionFinished(MQTTActionStatus.FAILED, e) },
            mqttEventsHandler
        )
    }

    fun forceDisconnect() {
        ExecutionHelper.executeMQTTClientAction(
            {
                mqttClient.disconnectForcibly()
            },
            { e -> androidConnectionCallback.disconnectActionFinished(MQTTActionStatus.FAILED, e) },
            mqttEventsHandler
        )
    }

    fun publish(topic: String, payload: ByteArray, qos: Int, retained: Boolean) {
        ExecutionHelper.executeMQTTClientAction(
            {
                mqttClient.publish(
                    topic,
                    payload,
                    qos,
                    retained,
                    MQTTActionContext(
                        action = MQTTAction.PUBLISH,
                        messagePayload = payload
                    ),
                    mqttActionListener
                )
            },
            { e ->
                androidConnectionCallback.publishActionFinished(
                    MQTTActionStatus.FAILED,
                    payload,
                    e
                )
            }, mqttEventsHandler
        )
    }

    fun subscribe(topicFilter: String, qos: Int) {
        ExecutionHelper.executeMQTTClientAction(
            {
                mqttClient.subscribe(
                    topicFilter,
                    qos,
                    MQTTActionContext(action = MQTTAction.SUBSCRIBE, topic = topicFilter),
                    mqttActionListener
                )
            },
            { e ->
                androidConnectionCallback.subscribeActionFinished(
                    MQTTActionStatus.FAILED,
                    topicFilter,
                    e
                )
            }, mqttEventsHandler
        )
    }

    fun subscribeMultiple(topicFilters: Array<String>, qos: IntArray) {
        ExecutionHelper.executeMQTTClientAction(
            {
                mqttClient.subscribe(
                    topicFilters,
                    qos,
                    MQTTActionContext(
                        action = MQTTAction.SUBSCRIBE_MULTIPLE,
                        topics = topicFilters
                    ),
                    mqttActionListener
                )
            },
            { e ->
                androidConnectionCallback.subscribeMultipleActionFinished(
                    MQTTActionStatus.FAILED,
                    topicFilters,
                    e
                )
            }, mqttEventsHandler
        )
    }
}