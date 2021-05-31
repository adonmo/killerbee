package com.adonmo.killerbee.rpc.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Request(val code: RequestCode, val payload: Parcelable) : Parcelable