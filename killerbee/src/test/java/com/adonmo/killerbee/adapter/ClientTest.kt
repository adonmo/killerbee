package com.adonmo.killerbee.adapter

import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTAction
import com.adonmo.killerbee.action.MQTTActionContext
import com.adonmo.killerbee.action.MQTTActionListener
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.helper.ConnectionHelper
import io.mockk.*
import io.mockk.impl.annotations.MockK
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
    fun connect_isWorkingAsExpected() {
        val exceptionOnConnect = Exception()
        every {
            mqttAsyncClient.connect(
                mqttConnectOptions,
                MQTTActionContext(action = MQTTAction.CONNECT, connectOptions),
                mqttActionListener
            )
        } throws exceptionOnConnect
        client.connect()
        verify { androidConnectionCallback.connectActionFinished(MQTTActionStatus.FAILED, connectOptions, exceptionOnConnect) }
    }

}