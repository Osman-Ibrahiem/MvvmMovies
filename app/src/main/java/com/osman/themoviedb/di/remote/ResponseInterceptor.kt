package com.osman.themoviedb.di.remote

import com.data.remote.model.RemoteResponse
import com.domain.core.RemoteException
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

class ResponseInterceptor @Inject constructor(
    val gson: Gson,
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response = chain.proceed(request)
        val code = response.code
        val body = response.body ?: throw RemoteException(code, "Server response is empty")

        if (!response.isSuccessful) {
            throw try {
                val error = gson.fromJson(body.string(),
                    RemoteResponse::class.java).errors?.lastOrNull()
                RemoteException(code, error)
            } catch (ex: Exception) {
                RemoteException(code, "Cannot parse error: ${ex.message}")
            } finally {
                body.close()
            }
        }

        val responseBody: ResponseBody = body.string().toResponseBody(body.contentType())
        return response.newBuilder()
            .body(body = responseBody)
            .build()

    }
}