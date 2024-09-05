package dev.fbvictorhugo.pin_bridge_sdk.data

import com.google.gson.annotations.SerializedName
import dev.fbvictorhugo.pin_bridge_sdk.data.enums.CreativeType
import dev.fbvictorhugo.pin_bridge_sdk.data.inners.PPinMedia

data class PPin(

    @SerializedName("id")
    val id: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("link")
    val link: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("dominant_color")
    val dominantColor: String?,

    @SerializedName("alt_text")
    val altText: String?,

    @SerializedName("creative_type")
    val creativeType: CreativeType?,

    @SerializedName("board_id")
    val boardId: String,

    @SerializedName("board_section_id")
    val boardSectionId: String?,

    @SerializedName("board_owner")
    val boardOwner: POwner,

    @SerializedName("is_owner")
    val isOwner: Boolean,

    @SerializedName("media")
    val media: PPinMedia,

    @SerializedName("parent_pin_id")
    val parentPinId: String?,

    @SerializedName("is_standard")
    val isStandard: Boolean,

    @SerializedName("has_been_promoted")
    val hasBeenPromoted: Boolean,

    @SerializedName("note")
    val note: String?

)