package com.adonmo.killerbee.action

import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.adapter.ConnectOptions
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.junit.Before
import org.junit.Test

class MQTTActionListenerTest {

    @MockK(relaxed = true)
    private lateinit var mqttConnectionCallback: IMQTTConnectionCallback

    @MockK(relaxed = true)
    private lateinit var connectOptions: ConnectOptions

    private lateinit var mqttActionListener: MQTTActionListener

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mqttActionListener = MQTTActionListener(null, mqttConnectionCallback)
    }

    @Test
    fun connectActionTests() {
        val asyncActionToken = mockk<IMqttToken>()

        every { asyncActionToken.userContext } returns MQTTActionContext(
            action = MQTTAction.CONNECT,
            connectOptions
        )

        mqttActionListener.onSuccess(asyncActionToken)
        verify(exactly = 1) {
            mqttConnectionCallback.connectActionFinished(
                MQTTActionStatus.SUCCESS,
                connectOptions,
                null
            )
        }

        val throwableOnFailure = Throwable("Failed")
        mqttActionListener.onFailure(asyncActionToken, throwableOnFailure)
        verify(exactly = 1) {
            mqttConnectionCallback.connectActionFinished(
                MQTTActionStatus.FAILED,
                connectOptions,
                match { it.localizedMessage == throwableOnFailure.localizedMessage })
        }
    }

    @Test
    fun disconnectActionTests() {
        val asyncActionToken = mockk<IMqttToken>()

        every { asyncActionToken.userContext } returns MQTTActionContext(action = MQTTAction.DISCONNECT)

        mqttActionListener.onSuccess(asyncActionToken)
        verify(exactly = 1) {
            mqttConnectionCallback.disconnectActionFinished(
                MQTTActionStatus.SUCCESS,
                null
            )
        }

        val throwableOnFailure = Throwable("Failed")
        mqttActionListener.onFailure(asyncActionToken, throwableOnFailure)
        verify(exactly = 1) {
            mqttConnectionCallback.disconnectActionFinished(
                MQTTActionStatus.FAILED,
                match { it.localizedMessage == throwableOnFailure.localizedMessage })
        }

    }

    @Test
    fun publishActionTests() {
        val asyncActionToken = mockk<IMqttToken>()
        val messagePayload = "Hello".toByteArray()

        every { asyncActionToken.userContext } returns MQTTActionContext(
            action = MQTTAction.PUBLISH,
            messagePayload = messagePayload
        )

        mqttActionListener.onSuccess(asyncActionToken)
        verify(exactly = 1) {
            mqttConnectionCallback.publishActionFinished(
                MQTTActionStatus.SUCCESS,
                messagePayload
            )
        }

        val throwableOnFailure = Throwable("Failed")
        mqttActionListener.onFailure(asyncActionToken, throwableOnFailure)
        verify(exactly = 1) {
            mqttConnectionCallback.publishActionFinished(
                MQTTActionStatus.FAILED,
                messagePayload,
                match { it.localizedMessage == throwableOnFailure.localizedMessage })
        }

    }

    @Test
    fun subscribeActionTests() {
        val asyncActionToken = mockk<IMqttToken>()
        val topic = "Jello"

        every { asyncActionToken.userContext } returns MQTTActionContext(
            action = MQTTAction.SUBSCRIBE,
            topic = topic
        )

        mqttActionListener.onSuccess(asyncActionToken)
        verify(exactly = 1) {
            mqttConnectionCallback.subscribeActionFinished(
                MQTTActionStatus.SUCCESS,
                topic
            )
        }

        val throwableOnFailure = Throwable("Failed")
        mqttActionListener.onFailure(asyncActionToken, throwableOnFailure)
        verify(exactly = 1) {
            mqttConnectionCallback.subscribeActionFinished(
                MQTTActionStatus.FAILED,
                topic,
                match { it.localizedMessage == throwableOnFailure.localizedMessage })
        }

    }

    @Test
    fun subscribeMultipleActionTests() {
        val asyncActionToken = mockk<IMqttToken>()
        val topics = arrayOf("Jello", "World")

        every { asyncActionToken.userContext } returns MQTTActionContext(
            action = MQTTAction.SUBSCRIBE_MULTIPLE,
            topics = topics
        )

        mqttActionListener.onSuccess(asyncActionToken)
        verify(exactly = 1) {
            mqttConnectionCallback.subscribeMultipleActionFinished(
                MQTTActionStatus.SUCCESS,
                topics
            )
        }

        val throwableOnFailure = Throwable("Failed")
        mqttActionListener.onFailure(asyncActionToken, throwableOnFailure)
        verify(exactly = 1) {
            mqttConnectionCallback.subscribeMultipleActionFinished(
                MQTTActionStatus.FAILED,
                topics,
                match { it.localizedMessage == throwableOnFailure.localizedMessage })
        }

    }


}