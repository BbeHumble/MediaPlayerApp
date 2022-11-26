package com.bbeHumble.media.player.data.repository

import com.bbeHumble.media.player.data.api.ApiService
import com.bbeHumble.media.player.data.model.VideoData
import javax.inject.Inject

class Repository @Inject internal constructor(var apiService: ApiService) {
    suspend fun loadVideos(): List<VideoData> {
        return apiService.loadVideos()
    }
}