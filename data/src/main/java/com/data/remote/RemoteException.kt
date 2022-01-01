package com.data.remote

import com.data.remote.model.RemoteResponse
import com.google.gson.Gson
import okhttp3.ResponseBody

fun ResponseBody.getMessage(): String {
    val messageText = this.string()
    return try {
        val model: RemoteResponse<*> = Gson().fromJson(messageText, RemoteResponse::class.java)
        model.errors?.lastOrNull() ?: ""
    } catch (e: Exception) {
        messageText
    }
}

fun ResponseBody.getMessage(model: RemoteResponse<*>): String {
    return model.errors?.lastOrNull() ?: ""
}

