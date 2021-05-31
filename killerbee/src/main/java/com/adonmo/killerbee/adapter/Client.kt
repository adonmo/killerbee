package com.adonmo.killerbee.adapter

import android.os.Messenger
import org.eclipse.paho.client.mqttv3.MqttClient

class Client(private val mqttClient: MqttClient, private val replyTo: Messenger) {
    private var status: ConnectionStatus = ConnectionStatus.DISCONNECTED


}