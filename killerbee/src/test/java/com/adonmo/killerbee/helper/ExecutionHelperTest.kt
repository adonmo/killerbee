package com.adonmo.killerbee.helper

import android.os.Handler
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.eclipse.paho.client.mqttv3.MqttException
import org.junit.Before
import org.junit.Test

class ExecutionHelperTest {

    @MockK(relaxed = true)
    private lateinit var mqttEventsHandler: Handler

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun executeCallback_PostsToHandler() {
        val lambdaArg = {}
        ExecutionHelper.executeCallback(mqttEventsHandler, lambdaArg)

        verify(exactly = 1) { mqttEventsHandler.post(any()) }
    }

    @Test
    fun executeCallback_DirectlyExecutesWhenNoHandlerGiven() {
        var a = 2
        val expectedValueOfA = 3
        val lambdaArg = { a = expectedValueOfA }
        ExecutionHelper.executeCallback(null, lambdaArg)

        assert(a == expectedValueOfA)
    }

    @Test
    fun executeMQTTClientAction_WithMQTTException() {
        val exceptionThrown = MqttException(Exception("Wrong Param"))
        val actionMethod = { throw exceptionThrown }

        var exceptionReceived: Exception? = null
        val callbackMethod: (e: Exception) -> Unit = { e -> exceptionReceived = e }

        ExecutionHelper.executeMQTTClientAction(actionMethod, callbackMethod)
        assert(exceptionThrown == exceptionReceived)
    }

    @Test
    fun executeMQTTClientAction_WithException() {
        val exceptionThrown = Exception("Wrong Param")
        val actionMethod = { throw exceptionThrown }

        var exceptionReceived: Exception? = null
        val callbackMethod: (e: Exception) -> Unit = { e -> exceptionReceived = e }

        ExecutionHelper.executeMQTTClientAction(actionMethod, callbackMethod)
        assert(exceptionThrown == exceptionReceived)
    }

    @Test
    fun executeMQTTClientAction() {
        var a = 3
        val expectedValueOfA = 5
        val actionMethod = { a = expectedValueOfA }

        var exceptionReceived: Exception? = null
        val callbackMethod: (e: Exception) -> Unit = { e -> exceptionReceived = e }

        ExecutionHelper.executeMQTTClientAction(actionMethod, callbackMethod)
        assert(null == exceptionReceived)
        assert(a == expectedValueOfA)
    }


}