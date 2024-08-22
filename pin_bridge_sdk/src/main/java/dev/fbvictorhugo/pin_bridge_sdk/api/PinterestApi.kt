package dev.fbvictorhugo.pin_bridge_sdk.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PinterestApi {
    @GET("boards")
    fun getBoards(
        @Header("Authorization") token: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Header("Accept") accept: String = "application/json"
    ): Call<String>
}