package dev.fbvictorhugo.pin_bridge_sdk.api

interface PCallback<T> {

    fun onSuccessful(response: PResponse<T>) {}

    fun onUnsuccessful(response: PResponse<T>) {}

    fun onFailure(t: Throwable) {}

}