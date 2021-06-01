package com.adonmo.killerbee.service

import android.app.Service
import android.content.Intent
import android.os.HandlerThread
import android.os.IBinder
import android.os.Messenger

class AndroidMQTTService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        val thread = HandlerThread("mqtt-bg-service")
        thread.start()
        return (Messenger(RPCHandler(thread.looper))).binder
    }
}