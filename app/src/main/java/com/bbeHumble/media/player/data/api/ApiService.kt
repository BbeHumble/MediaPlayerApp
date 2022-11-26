package com.bbeHumble.media.player.data.api

import com.bbeHumble.media.player.data.model.VideoData
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api/backgrounds")
    suspend fun loadVideos(
        @Query("group") group: String = "video",
        @Query("category_id") categoryId: Int = 1
    ): List<VideoData>
}