package com.adonmo.killerbee.adapter

import java.util.*
import javax.net.SocketFactory
import javax.net.ssl.HostnameVerifier


data class ConnectOptions(
    val clientID: String,
    val serverURI: String,
    val username: String? = null,
    val password: String? = null,
    val keepAliveInterval: Int = 60,
    val maxInFlight: Int = 10,
    val willDestination: String? = null,
    val willMessagePayload: ByteArray? = null,
    val willQos: Int = 1,
    val willRetained: Boolean = true,
    val socketFactory: SocketFactory? = null,
    val sslClientProps: Properties? = null,
    val httpsHostnameVerificationEnabled: Boolean = false,
    val sslHostnameVerifier: HostnameVerifier? = null,
    val cleanSession: Boolean = true,
    val connectionTimeout: Int = 30,
    val serverURIs: Array<String>? = null,
    val mqttVersion: Int = 0,
    val automaticReconnect:Boolean = true,
    val maxReconnectDelay: Int = 128000,
    val customWebSocketHeaders: Properties? = null,
    val executorServiceTimeout: Int = 1,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConnectOptions

        if (clientID != other.clientID) return false
        if (serverURI != other.serverURI) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (keepAliveInterval != other.keepAliveInterval) return false
        if (maxInFlight != other.maxInFlight) return false
        if (willDestination != other.willDestination) return false
        if (willMessagePayload != null) {
            if (other.willMessagePayload == null) return false
            if (!willMessagePayload.contentEquals(other.willMessagePayload)) return false
        } else if (other.willMessagePayload != null) return false
        if (socketFactory != other.socketFactory) return false
        if (sslClientProps != other.sslClientProps) return false
        if (httpsHostnameVerificationEnabled != other.httpsHostnameVerificationEnabled) return false
        if (sslHostnameVerifier != other.sslHostnameVerifier) return false
        if (cleanSession != other.cleanSession) return false
        if (connectionTimeout != other.connectionTimeout) return false
        if (serverURIs != null) {
            if (other.serverURIs == null) return false
            if (!serverURIs.contentEquals(other.serverURIs)) return false
        } else if (other.serverURIs != null) return false
        if (mqttVersion != other.mqttVersion) return false
        if (automaticReconnect != other.automaticReconnect) return false
        if (maxReconnectDelay != other.maxReconnectDelay) return false
        if (customWebSocketHeaders != other.customWebSocketHeaders) return false
        if (executorServiceTimeout != other.executorServiceTimeout) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clientID.hashCode()
        result = 31 * result + serverURI.hashCode()
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + keepAliveInterval
        result = 31 * result + maxInFlight
        result = 31 * result + (willDestination?.hashCode() ?: 0)
        result = 31 * result + (willMessagePayload?.contentHashCode() ?: 0)
        result = 31 * result + (socketFactory?.hashCode() ?: 0)
        result = 31 * result + (sslClientProps?.hashCode() ?: 0)
        result = 31 * result + httpsHostnameVerificationEnabled.hashCode()
        result = 31 * result + (sslHostnameVerifier?.hashCode() ?: 0)
        result = 31 * result + cleanSession.hashCode()
        result = 31 * result + connectionTimeout
        result = 31 * result + (serverURIs?.contentHashCode() ?: 0)
        result = 31 * result + mqttVersion
        result = 31 * result + automaticReconnect.hashCode()
        result = 31 * result + maxReconnectDelay
        result = 31 * result + (customWebSocketHeaders?.hashCode() ?: 0)
        result = 31 * result + executorServiceTimeout
        return result
    }

}