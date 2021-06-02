package com.adonmo.killerbee.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConnectOptions(
    val clientID: String,
    val serverURI: String,
    val username: String? = null,
    val password: String? = null
) : Parcelable