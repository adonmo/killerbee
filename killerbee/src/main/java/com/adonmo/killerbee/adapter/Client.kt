package com.adonmo.killerbee.adapter

import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTAction
import com.adonmo.killerbee.action.MQTTActionContext
import com.adonmo.killerbee.action.MQTTActionListener
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ConnectionHelper
import com.adonmo.killerbee.helper.ExecutionHelper
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.TimerPingSender
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.concurrent.ScheduledExecutorService

class Client(
    private val connectOptions: ConnectOptions,
    clientCallback: ClientCallback,
    private val mqttActionListener: MQTTActionListener,
    executorService: ScheduledExecutorService?,
    private val androidConnectionCallback: IMQTTConnectionCallback
) {
    private val mqttClient: MqttAsyncClient =
        MqttAsyncClient(
            connectOptions.serverURI,
            connectOptions.clientID,
            MemoryPersistence(),
            TimerPingSender(),
            executorService
        )

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
            })
    }

    fun disconnect() {
        ExecutionHelper.executeMQTTClientAction({
            mqttClient.disconnect(
                MQTTActionContext(action = MQTTAction.DISCONNECT),
                mqttActionListener
            )
        }, { e -> androidConnectionCallback.disconnectActionFinished(MQTTActionStatus.FAILED, e) })
    }

    fun forceDisconnect() {
        ExecutionHelper.executeMQTTClientAction({
            mqttClient.disconnectForcibly()
        }, { e -> androidConnectionCallback.disconnectActionFinished(MQTTActionStatus.FAILED, e) })
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
            })
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
            })
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
            })
    }
}