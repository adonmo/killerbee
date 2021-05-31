package com.adonmo.killerbee.service

import android.os.Handler
import android.os.Message
import android.util.Log
import com.adonmo.killerbee.Constants.Companion.LOG_TAG
import com.adonmo.killerbee.rpc.request.Helper

class RPCHandler : Handler() {
    override fun handleMessage(msg: Message) {
        val request = Helper.getRPCRequestFromMessage(msg)
        Log.v(LOG_TAG, "Received rpc request [$request]")
        super.handleMessage(msg)
    }
}