package com.adonmo.killerbee.helper

import com.adonmo.killerbee.adapter.ConnectOptions
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.junit.Assert.assertEquals
import org.junit.Test

class ConnectionHelperTest {

    @Test
    fun getMqttConnectOptions_WithoutWill() {
        val connectOptions = ConnectOptions(clientID = "Hello", serverURI = "World")
        val mqttConnectOptions = MqttConnectOptions()

        assertEquals(ConnectionHelper.getMqttConnectOptions(connectOptions).debug, mqttConnectOptions.debug)
    }


    @Test
    fun getMqttConnectOptions_WithWill() {
        val connectOptions = ConnectOptions(clientID = "Hello", serverURI = "World", willDestination = "Hulu", willMessagePayload = "Hi".toByteArray())
        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.setWill(connectOptions.willDestination, connectOptions.willMessagePayload, 1, true)

        assertEquals(ConnectionHelper.getMqttConnectOptions(connectOptions).debug, mqttConnectOptions.debug)
    }

}