package com.adonmo.killerbee

import android.os.Handler
import com.adonmo.killerbee.adapter.Client
import com.adonmo.killerbee.adapter.ConnectOptions
import com.adonmo.killerbee.helper.ConnectionHelper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.concurrent.ScheduledExecutorService

class AndroidMQTTClientTest {
    @MockK(relaxed = true)
    private lateinit var connectOptions: ConnectOptions

    @MockK(relaxed = true)
    private lateinit var mqttEventsHandler: Handler

    @MockK(relaxed = true)
    private lateinit var androidMqttActionCallback: IMQTTConnectionCallback

    @MockK(relaxed = true)
    private lateinit var executorService: ScheduledExecutorService

    @MockK(relaxed = true)
    private lateinit var mqttClientAdapter: Client

    private lateinit var androidMQTTClient: AndroidMQTTClient

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockkObject(ConnectionHelper)
        every {
            ConnectionHelper.getMqttClientAdapter(
                connectOptions,
                mqttEventsHandler,
                androidMqttActionCallback,
                executorService
            )
        } returns mqttClientAdapter

        androidMQTTClient = AndroidMQTTClient(
            connectOptions,
            mqttEventsHandler,
            androidMqttActionCallback,
            executorService
        )
    }

    @Test
    fun checkAllActions() {
        androidMQTTClient.connect()
        verify(exactly = 1) { mqttClientAdapter.connect() }

        androidMQTTClient.disconnect()
        verify(exactly = 1) { mqttClientAdapter.disconnect() }

        androidMQTTClient.forceDisconnect()
        verify(exactly = 1) { mqttClientAdapter.forceDisconnect() }

        val topic = "Hello"
        val payload = "World".toByteArray()
        val qos = 1
        val retained = false
        androidMQTTClient.publish(topic, payload, qos, retained)
        verify(exactly = 1) { mqttClientAdapter.publish(topic, payload, qos, retained) }

        androidMQTTClient.subscribe(topic, qos)
        verify(exactly = 1) { mqttClientAdapter.subscribe(topic, qos) }

        val topics = arrayOf("Bye","Hi")
        val qosValues = intArrayOf(1, 0)
        androidMQTTClient.subscribe(topics, qosValues)
        verify(exactly = 1) { mqttClientAdapter.subscribeMultiple(topics, qosValues) }
    }
}