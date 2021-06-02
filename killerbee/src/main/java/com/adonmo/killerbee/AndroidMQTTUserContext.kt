package com.adonmo.killerbee

import com.adonmo.killerbee.adapter.ConnectOptions

data class AndroidMQTTUserContext(
    val action: AndroidMqttAction,
    val connectOptions: ConnectOptions? = null,
    val topic: String? = null,
    val topics: Array<String>? = null,
    val messagePayload: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndroidMQTTUserContext

        if (action != other.action) return false
        if (connectOptions != other.connectOptions) return false
        if (topic != other.topic) return false
        if (topics != null) {
            if (other.topics == null) return false
            if (!topics.contentEquals(other.topics)) return false
        } else if (other.topics != null) return false
        if (messagePayload != null) {
            if (other.messagePayload == null) return false
            if (!messagePayload.contentEquals(other.messagePayload)) return false
        } else if (other.messagePayload != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = action.hashCode()
        result = 31 * result + (connectOptions?.hashCode() ?: 0)
        result = 31 * result + (topic?.hashCode() ?: 0)
        result = 31 * result + (topics?.contentHashCode() ?: 0)
        result = 31 * result + (messagePayload?.contentHashCode() ?: 0)
        return result
    }

}