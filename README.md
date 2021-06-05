# killerbee
MQTT android client

## Adding as a Dependency
This package is currently hosted as a github package. Github currently supports public package hosting but requires github personal access token to fetch them.
In short you need to have a github account to use this as a dependency.

### Setting up github credentials
* Generate an github personal access token with repo actions access. 
* Create a file `github.properties` in project root. 
* Add `github.properties filename to `.gitgnore` to avoid adding it to version control.
* Add the github values to the `github.properties` file
```bash
GITHUB_USER=
GITHUB_PERSONAL_ACCESS_TOKEN=
```

### Add maven repository for github package

Add the maven repo for `KillerBee` as shown below in `build.gradle` of the project root.
```groovy
def githubProperties = new Properties()
githubProperties.load(new FileInputStream(rootProject.file("github.properties")))

allprojects {
    repositories {
        google()
        mavenCentral()
        //Use for local testing of library after assemble and publishToMavenLocal
        //mavenLocal()

        maven {
            name = "KillerBee"
            url = uri("https://maven.pkg.github.com/adonmo/killerbee")
            credentials {
                username = githubProperties['GITHUB_USER']
                password = githubProperties['GITHUB_PERSONAL_ACCESS_TOKEN']
            }
        }
    }
}
```
### Add dependency to build.gradle in app folder
```groovy
implementation 'com.adonmo.libraries:killerbee:1.0.0'
```

## Sample Implementation
```java
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
import com.adonmo.killerbee.helper.Constants.LOG_TAG
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
        throwable: Throwable?
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

    override fun disconnectActionFinished(status: MQTTActionStatus, throwable: Throwable?) {
        Log.d(LOG_TAG, "Disconnect Action Status: [$status]")
    }

    override fun publishActionFinished(
        status: MQTTActionStatus,
        messagePayload: ByteArray,
        throwable: Throwable?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            Log.d(LOG_TAG, "Published message $messagePayload")
        }
    }

    override fun subscribeActionFinished(
        status: MQTTActionStatus,
        topic: String,
        throwable: Throwable?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            mqttClient.publish("HelloBee", "World".toByteArray(), 1, false)
        }
    }

    override fun subscribeMultipleActionFinished(
        status: MQTTActionStatus,
        topics: Array<String>,
        throwable: Throwable?
    ) {
        if (status == MQTTActionStatus.SUCCESS) {
            mqttClient.publish("HelloBee", "World".toByteArray(), 1, false)
        }
    }

    override fun connectionLost(connectOptions: ConnectOptions, throwable: Throwable?) {
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
```

