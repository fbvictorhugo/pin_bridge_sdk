package dev.fbvictorhugo.pin_bridge_sdk.data

import com.google.gson.annotations.SerializedName

data class PMedia(

    @SerializedName("image_cover_url")
    val imageCoverUrl: String,

    @SerializedName("pin_thumbnail_urls")
    val pinThumbnailUrls: List<String>

)