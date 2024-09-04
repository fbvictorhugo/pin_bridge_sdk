package dev.fbvictorhugo.pin_bridge_sdk.data.inners

import com.google.gson.annotations.SerializedName

data class PBoardMedia(

    @SerializedName("image_cover_url")
    val imageCoverUrl: String,

    @SerializedName("pin_thumbnail_urls")
    val pinThumbnailUrls: List<String>

)