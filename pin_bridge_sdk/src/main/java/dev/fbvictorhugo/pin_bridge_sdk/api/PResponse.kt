package dev.fbvictorhugo.pin_bridge_sdk.api

import retrofit2.Response

class PResponse<T>(private val response: Response<T>) {

    fun getCode(): Int {
        return response.code()
    }

    fun getMessage(): String {
        return response.message()
    }

    fun getObject(): T? {
        return response.body()
    }

}