package com.ramdani.danamon.data.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class  GeneralResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: Message? = null,
) {
    data class Message(
        @SerializedName("en")
        val en: String,
        @SerializedName("id")
        val id: String
    )
}