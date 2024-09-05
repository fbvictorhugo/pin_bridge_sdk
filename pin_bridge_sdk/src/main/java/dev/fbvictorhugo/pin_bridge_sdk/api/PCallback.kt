package dev.fbvictorhugo.pin_bridge_sdk.api

interface PCallback<PModel> {

    fun onSuccess(response: PResponse<PModel>?) {

    }

    fun onFailure(exception: PException?) {

    }

}