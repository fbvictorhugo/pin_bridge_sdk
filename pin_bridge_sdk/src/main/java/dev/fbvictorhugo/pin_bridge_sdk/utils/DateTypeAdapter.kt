package dev.fbvictorhugo.pin_bridge_sdk.utils

import com.google.gson.*
import java.text.SimpleDateFormat
import java.util.*

class DateTypeAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun serialize(src: Date, typeOfSrc: java.lang.reflect.Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(dateFormat.format(src))
    }

    override fun deserialize(json: JsonElement, typeOfT: java.lang.reflect.Type, context: JsonDeserializationContext): Date {
        return dateFormat.parse(json.asString)
    }
}
