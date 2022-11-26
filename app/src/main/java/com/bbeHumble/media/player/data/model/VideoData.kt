package com.bbeHumble.media.player.data.model

import com.google.gson.annotations.SerializedName

data class VideoData(
    @SerializedName("id")
    val id: String,
    @SerializedName("small_thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("file_url")
    val fileUrl: String
)
