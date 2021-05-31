package com.adonmo.killerbee.rpc.request

data class ConnectOptions(
    val serverURI: String, val username: String?, val password: String?, val clientID: String?
)