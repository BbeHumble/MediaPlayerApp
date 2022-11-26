package com.bbeHumble.media.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bbeHumble.media.player.data.repository.Repository
import com.bbeHumble.media.player.di.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: Repository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainActivity.MainScreenState>(
        MainActivity.MainScreenState.Loading
    )
    val uiState: StateFlow<MainActivity.MainScreenState> = _uiState

    init {
        loadVideos()
    }

    private fun loadVideos() {
        viewModelScope.launch(coroutineDispatcherProvider.IO()) {
            try {
                val loadedVideos = repository.loadVideos()
                _uiState.value = MainActivity.MainScreenState.Success(loadedVideos)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}