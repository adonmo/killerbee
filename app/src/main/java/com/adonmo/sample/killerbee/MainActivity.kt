package com.adonmo.sample.killerbee

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.adonmo.killerbee.AndroidMQTTClient
import com.adonmo.killerbee.IMQTTConnectionCallback
import com.adonmo.killerbee.action.MQTTActionStatus
import com.adonmo.killerbee.adapter.ConnectOptions
import com.adonmo.killerbee.helper.Constants.Companion.LOG_TAG
import java.util.concurrent.ScheduledThreadPoolExecutor

class MainActivity : AppCompatActivity(), IMQTTConnectionCallback {
    private lateinit var mqttThread: HandlerThread
    private lateinit var mqttHandler: Handler

    private lateinit var mqttClient: AndroidMQTTClient
    private lateinit var executor: ScheduledThreadPoolExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(LOG_TAG, "Running on thread [${Thread.currentThread()}]")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttThread = HandlerThread("mqttThread")
        mqttThread.start()
        mqttHandler = Handler(mqttThread.looper)

        /* As it stands a minimum of 4 threads seems to be necessary to let the MQTT client run
            as it blocks a few of them(3 based on testing) with a looper  most likely */
        executor = ScheduledThreadPoolExecutor(4)
        mqttClient = AndroidMQTTClient(
            ConnectOptions(
                clientID = "OG",
                serverURI = "tcp://broker.hivemq.com:1883",
                cleanSession = true,
                keepAliveInterval = 30,
                maxReconnectDelay = 60000,
                automaticReconnect = true,
            ),
            mqttHandler,
            this,
            executorService = executor
        )
        mqttClient.connect()
    }

    override fun connectActionFinished(
        status: MQTTActionStatus,
        connectOptions: ConnectOptions,
        exception: Exception?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            mqttClient.subscribe("Jello", 1)
            mqttClient.subscribe(arrayOf("HelloBee", "BeeHello"), intArrayOf(1, 0))
        } else {
            Log.e(
                LOG_TAG,
                "Connection Action Failed for [${connectOptions.clientID}] to [${connectOptions.serverURI}]"
            )
        }
    }

    override fun disconnectActionFinished(status: MQTTActionStatus, exception: Exception?) {
        Log.d(LOG_TAG, "Disconnect Action Status: [$status]")
    }

    override fun publishActionFinished(
        status: MQTTActionStatus,
        messagePayload: ByteArray,
        exception: Exception?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            Log.d(LOG_TAG, "Published message $messagePayload")
        }
    }

    override fun subscribeActionFinished(
        status: MQTTActionStatus,
        topic: String,
        exception: Exception?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            mqttClient.publish("HelloBee", "World".toByteArray(), 1, false)
        }
    }

    override fun subscribeMultipleActionFinished(
        status: MQTTActionStatus,
        topics: Array<String>,
        exception: Exception?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            mqttClient.publish("HelloBee", "World".toByteArray(), 1, false)
        }
    }

    override fun connectionLost(connectOptions: ConnectOptions) {
        Log.d(
            LOG_TAG,
            "Connection lost for [${connectOptions.clientID}] from [${connectOptions.serverURI}]"
        )
    }

    override fun messageArrived(
        topic: String?,
        message: ByteArray?
    ) {
        message?.let {
            Log.d(LOG_TAG, "Received message [$message]")
        }
        //mqttClient.disconnect()
    }
}