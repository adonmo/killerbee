package com.adonmo.killerbee.helper

import com.adonmo.killerbee.adapter.ConnectOptions
import io.mockk.every
import io.mockk.mockk
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.TimerPingSender
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import javax.net.SocketFactory
import javax.net.ssl.HostnameVerifier

class ConnectionHelperTest {

    @Test
    fun getMqttConnectOptions_WithoutWill() {
        val connectOptions = ConnectOptions(clientID = "Hello", serverURI = "World")
        val mqttConnectOptions = MqttConnectOptions()

        assertEquals(
            ConnectionHelper.getMqttConnectOptions(connectOptions).debug,
            mqttConnectOptions.debug
        )
    }


    @Test
    fun getMqttConnectOptions_WithWill() {
        val connectOptions = ConnectOptions(
            clientID = "Hello",
            serverURI = "World",
            willDestination = "Hulu",
            willMessagePayload = "Hi".toByteArray(),
            customWebSocketHeaders = Properties(),
            serverURIs = arrayOf(),
            sslHostnameVerifier = HostnameVerifier { _, _ -> false },
            socketFactory = SocketFactory.getDefault(),
            sslClientProps = Properties(),
            username = "Hello",
            password = "World"
        )
        val expectedMqttConnectOptions = MqttConnectOptions()
        expectedMqttConnectOptions.setWill(
            connectOptions.willDestination,
            connectOptions.willMessagePayload,
            1,
            true
        )
        expectedMqttConnectOptions.sslHostnameVerifier = connectOptions.sslHostnameVerifier
        expectedMqttConnectOptions.customWebSocketHeaders = connectOptions.customWebSocketHeaders
        expectedMqttConnectOptions.serverURIs = connectOptions.serverURIs
        expectedMqttConnectOptions.socketFactory = connectOptions.socketFactory
        expectedMqttConnectOptions.sslProperties = connectOptions.sslClientProps
        expectedMqttConnectOptions.userName = connectOptions.username
        expectedMqttConnectOptions.password = connectOptions.password?.toCharArray()

        assertEquals(
            ConnectionHelper.getMqttConnectOptions(connectOptions).debug,
            expectedMqttConnectOptions.debug
        )
    }

    @Test
    fun getMqttAsyncClient() {
        val connectOptions: ConnectOptions = mockk(relaxed = true)
        val executorService: ScheduledExecutorService = mockk(relaxed = true)

        val serverURI = "wss://best.org/mqtt"
        val clientID = "World"

        every { connectOptions.serverURI } returns serverURI
        every { connectOptions.clientID } returns clientID

        //TODO Better test with deep equality
        val mqttAsyncClient = ConnectionHelper.getMqttAsyncClient(connectOptions, executorService)
        assertEquals(mqttAsyncClient.serverURI, serverURI)
    }

}