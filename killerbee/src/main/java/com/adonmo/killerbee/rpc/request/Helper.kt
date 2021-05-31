package com.adonmo.killerbee.rpc.request

import android.os.Bundle
import android.os.Message
import android.os.Messenger
import android.os.Parcelable

class Helper {
    companion object {
        fun getMessageForRPC(request: Request, replyTo: Messenger): Message {
            val message = Message.obtain(null, request.code.ordinal)
            val bundle = Bundle()
            bundle.putParcelable(Request::class.simpleName, request)
            message.data = bundle
            message.replyTo = replyTo
            return message
        }

        fun getRPCRequestFromMessage(message: Message): Request? {
            val bundle = message.data
            var request: Request? = null
            bundle?.let {
                val payload: Parcelable? = it.getParcelable(Request::class.simpleName)
                request =  payload as Request
            }

            return request
        }
    }
}