package com.adonmo.killerbee.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Messenger

class AndroidMQTTService : Service() {

    private lateinit var remoteMessenger: Messenger

    override fun onCreate() {
        super.onCreate()
        remoteMessenger = Messenger(RPCHandler())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return remoteMessenger.binder
    }


}