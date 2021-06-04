package com.adonmo.killerbee.adapter

import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTAction
import com.adonmo.killerbee.action.MQTTActionContext
import com.adonmo.killerbee.action.MQTTActionListener
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ConnectionHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.verify
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.junit.Before
import org.junit.Test
import java.util.concurrent.ScheduledExecutorService

class ClientTest {
    @MockK(relaxed = true)
    private lateinit var connectOptions: ConnectOptions

    @MockK(relaxed = true)
    private lateinit var clientCallback: ClientCallback

    @MockK(relaxed = true)
    private lateinit var mqttActionListener: MQTTActionListener

    @MockK(relaxed = true)
    private lateinit var executorService: ScheduledExecutorService

    @MockK(relaxed = true)
    private lateinit var androidConnectionCallback: IMQTTConnectionCallback

    @MockK(relaxed = true)
    private lateinit var mqttConnectOptions: MqttConnectOptions

    @MockK(relaxed = true)
    private lateinit var mqttAsyncClient: MqttAsyncClient

    private lateinit var client: Client

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(ConnectionHelper)

        every {
            ConnectionHelper.getMqttAsyncClient(
                connectOptions,
                executorService
            )
        } returns mqttAsyncClient

        every {
            ConnectionHelper.getMqttConnectOptions(connectOptions)
        } returns mqttConnectOptions

        client = Client(
            connectOptions,
            clientCallback,
            mqttActionListener,
            executorService,
            androidConnectionCallback
        )

    }

    @Test
    fun connect_TriggersCallbackOnError() {
        val exceptionOnConnect = Exception()
        every {
            mqttAsyncClient.connect(
                mqttConnectOptions,
                MQTTActionContext(action = MQTTAction.CONNECT, connectOptions),
                mqttActionListener
            )
        } throws exceptionOnConnect
        client.connect()
        verify(exactly = 1) {
            androidConnectionCallback.connectActionFinished(
                MQTTActionStatus.FAILED,
                connectOptions,
                exceptionOnConnect
            )
        }
    }

    @Test
    fun connect() {
        client.connect()
        verify(exactly = 0) {
            androidConnectionCallback.connectActionFinished(
                MQTTActionStatus.FAILED,
                connectOptions,
                any()
            )
        }
    }

    @Test
    fun disconnect_TriggersCallbackOnError() {
        val exceptionOnDisconnect = Exception()
        every {
            mqttAsyncClient.disconnect(
                MQTTActionContext(action = MQTTAction.DISCONNECT),
                mqttActionListener
            )
        } throws exceptionOnDisconnect
        client.disconnect()
        verify(exactly = 1) {
            androidConnectionCallback.disconnectActionFinished(
                MQTTActionStatus.FAILED,
                exceptionOnDisconnect
            )
        }
    }

    @Test
    fun disconnect() {
        client.disconnect()
        verify(exactly = 0) {
            androidConnectionCallback.disconnectActionFinished(
                MQTTActionStatus.FAILED,
                any()
            )
        }
    }

    @Test
    fun forceDisconnect_TriggersCallbackOnError() {
        val exceptionOnDisconnect = Exception()
        every {
            mqttAsyncClient.disconnectForcibly()
        } throws exceptionOnDisconnect
        client.forceDisconnect()
        verify(exactly = 1) {
            androidConnectionCallback.disconnectActionFinished(
                MQTTActionStatus.FAILED,
                exceptionOnDisconnect
            )
        }
    }

    @Test
    fun forceDisconnect() {
        client.forceDisconnect()
        verify(exactly = 0) {
            androidConnectionCallback.disconnectActionFinished(
                MQTTActionStatus.FAILED,
                any()
            )
        }
    }

    @Test
    fun publish_TriggersCallbackOnError() {
        val exceptionOnPublish = Exception()
        val topic = "ABC"
        val payload = "DEF".toByteArray()
        val qos = 0
        val retained = false
        every {
            mqttAsyncClient.publish(
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
        } throws exceptionOnPublish
        client.publish(topic, payload, qos, retained)
        verify(exactly = 1) {
            androidConnectionCallback.publishActionFinished(
                MQTTActionStatus.FAILED,
                payload,
                exceptionOnPublish
            )
        }
    }

    @Test
    fun publish() {
        val topic = "ABC"
        val payload = "DEF".toByteArray()
        val qos = 0
        val retained = false
        client.publish(topic, payload, qos, retained)
        verify(exactly = 0) {
            androidConnectionCallback.publishActionFinished(
                MQTTActionStatus.FAILED,
                any(),
                any()
            )
        }
    }

    @Test
    fun subscribe_TriggersCallbackOnError() {
        val exceptionOnSubscribe = Exception()
        val topicFilter = "ABC"
        val qos = 0
        every {
            mqttAsyncClient.subscribe(
                topicFilter,
                qos,
                MQTTActionContext(action = MQTTAction.SUBSCRIBE, topic = topicFilter),
                mqttActionListener
            )
        } throws exceptionOnSubscribe
        client.subscribe(topicFilter, qos)
        verify(exactly = 1) {
            androidConnectionCallback.subscribeActionFinished(
                MQTTActionStatus.FAILED,
                topicFilter,
                exceptionOnSubscribe
            )
        }
    }

    @Test
    fun subscribe() {
        val topicFilter = "ABC"
        val qos = 0
        client.subscribe(topicFilter, qos)
        verify(exactly = 0) {
            androidConnectionCallback.subscribeActionFinished(
                MQTTActionStatus.FAILED,
                any(),
                any()
            )
        }
    }

    @Test
    fun subscribeMultiple_TriggersCallbackOnError() {
        val exceptionOnSubscribe = Exception()
        val topicFilters = arrayOf("ABC", "DEF")
        val qos = intArrayOf(1, 0)
        every {
            mqttAsyncClient.subscribe(
                topicFilters,
                qos,
                MQTTActionContext(action = MQTTAction.SUBSCRIBE_MULTIPLE, topics = topicFilters),
                mqttActionListener
            )
        } throws exceptionOnSubscribe
        client.subscribeMultiple(topicFilters, qos)
        verify(exactly = 1) {
            androidConnectionCallback.subscribeMultipleActionFinished(
                MQTTActionStatus.FAILED,
                topicFilters,
                exceptionOnSubscribe
            )
        }
    }

    @Test
    fun subscribeMultiple() {
        val topicFilters = arrayOf("ABC", "DEF")
        val qos = intArrayOf(1, 0)
        client.subscribeMultiple(topicFilters, qos)
        verify(exactly = 0) {
            androidConnectionCallback.subscribeMultipleActionFinished(
                MQTTActionStatus.FAILED,
                any(),
                any()
            )
        }
    }
}