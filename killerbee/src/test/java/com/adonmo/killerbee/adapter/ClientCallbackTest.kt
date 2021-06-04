package com.adonmo.killerbee.adapter

import android.os.Handler
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ExecutionHelper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.junit.Before
import org.junit.Test

class ClientCallbackTest {

    private val mqttEventsHandler: Handler? = null

    @MockK(relaxed = true)
    private lateinit var actionCallback: IMQTTConnectionCallback

    @MockK(relaxed = true)
    private lateinit var connectOptions: ConnectOptions

    private lateinit var clientCallback: ClientCallback

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        clientCallback = ClientCallback(mqttEventsHandler, actionCallback, connectOptions)
        mockkObject(ExecutionHelper)
    }

    @Test
    fun connectionLost() {
        clientCallback.connectionLost(Exception())
        verify(exactly = 1) { actionCallback.connectionLost(connectOptions) }
    }

    @Test
    fun messageArrived() {
        val topic = ""
        val message: MqttMessage? = null

        clientCallback.messageArrived(topic, message)
        verify(exactly = 1) {
            actionCallback.messageArrived(
                topic,
                null
            )
        }
    }


    @Test
    fun deliveryComplete() {
        clientCallback.deliveryComplete(null)
        clientCallback.deliveryComplete(mockk())
    }

    @Test
    fun connectComplete_WithAutoReconnectBeingTheCause() {
        val reconnect = true
        val serverURI = "hello"

        clientCallback.connectComplete(reconnect, serverURI)
        verify(exactly = 1) {
            actionCallback.connectActionFinished(
                MQTTActionStatus.SUCCESS,
                connectOptions
            )
        }
    }

    @Test
    fun connectComplete_WithConnectAction() {
        val reconnect = false
        val serverURI = "hello"

        clientCallback.connectComplete(reconnect, serverURI)
        verify(exactly = 0) {
            actionCallback.connectActionFinished(
                any(),
                any()
            )
        }
    }


}