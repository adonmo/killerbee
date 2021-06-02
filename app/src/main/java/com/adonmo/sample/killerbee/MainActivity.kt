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
import com.adonmo.killerbee.helper.Constants
import java.util.concurrent.ScheduledThreadPoolExecutor

class MainActivity : AppCompatActivity(), IMQTTConnectionCallback {
    private lateinit var mqttThread: HandlerThread
    private lateinit var mqttHandler: Handler

    private lateinit var mqttClient: AndroidMQTTClient

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.v(Constants.LOG_TAG, "Running on thread [${Thread.currentThread()}]")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mqttThread = HandlerThread("mqttThread")
        mqttThread.start()
        mqttHandler = Handler(mqttThread.looper)

        mqttClient = AndroidMQTTClient(
            ConnectOptions(clientID = "OG", serverURI = "tcp://broker.hivemq.com:1883"),
            mqttHandler,
            this,
            executorService = ScheduledThreadPoolExecutor(2)
        )
    }

    override fun connectActionFinished(status: MQTTActionStatus, connectOptions: ConnectOptions) {
        TODO("Not yet implemented")
    }

    override fun disconnectActionFinished(status: MQTTActionStatus) {
        TODO("Not yet implemented")
    }

    override fun publishActionFinished(status: MQTTActionStatus, messagePayload: ByteArray) {
        TODO("Not yet implemented")
    }

    override fun subscribeActionFinished(status: MQTTActionStatus, topic: String) {
        TODO("Not yet implemented")
    }

    override fun subscribeMultipleActionFinished(status: MQTTActionStatus, topics: Array<String>) {
        TODO("Not yet implemented")
    }

    override fun connectionLost(connectOptions: ConnectOptions) {
        TODO("Not yet implemented")
    }

    override fun messageArrived(
        topic: String?,
        message: ByteArray?
    ) {
        TODO("Not yet implemented")
    }
}