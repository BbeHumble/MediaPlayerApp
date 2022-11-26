package com.bbeHumble.media.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbeHumble.media.player.data.model.VideoData
import com.bbeHumble.media.player.databinding.AcMainBinding
import com.bbeHumble.media.player.ui.videosList.VideosAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainActivityViewModel>()

    private var _binding: AcMainBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = AcMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        initPlayer()
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is MainScreenState.Loading -> {

                    }
                    is MainScreenState.Error -> {
                        Toast.makeText(applicationContext, state.message, Toast.LENGTH_LONG).show()
                    }
                    is MainScreenState.Success -> {
                        loadListOfVideos(state.videos)
                    }
                }
            }
        }
        binding.textButton.setOnClickListener {
            createMovableEdittext()
        }
        binding.videoPlayer.setOnClickListener {
            it.requestFocus()
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createMovableEdittext() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val movableEdittext = EditText(this)
        movableEdittext.layoutParams = params
        binding.videoContainer.addView(
            movableEdittext
        )
        movableEdittext.setBackgroundResource(android.R.color.transparent)
        requestFocusAndOpenKeyboard(movableEdittext)
        var dX = 0f
        var dY = 0f
        movableEdittext.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isInParentView(motionEvent, dX, movableEdittext, dY)
                    )
                        view.animate()
                            .x(motionEvent.rawX + dX)
                            .y(motionEvent.rawY + dY)
                            .setDuration(0)
                            .start()
                }
                else -> return@setOnTouchListener false
            }
            return@setOnTouchListener true


        }
    }

    private fun isInParentView(
        motionEvent: MotionEvent,
        dX: Float,
        movableEdittext: EditText,
        dY: Float
    ) = (binding.videoPlayer.left < motionEvent.rawX + dX) &&
            (motionEvent.rawX + dX + movableEdittext.width < binding.videoPlayer.right) &&
            (motionEvent.rawY + dY - movableEdittext.height > binding.videoPlayer.top - movableEdittext.height) &&
            (motionEvent.rawY + dY + movableEdittext.height < binding.videoPlayer.bottom)

    private fun requestFocusAndOpenKeyboard(myEditText: EditText) {
        myEditText.requestFocus()
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(myEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun initPlayer() {
        val player = ExoPlayer.Builder(this).build()
        binding.videoPlayer.player = player
        player.repeatMode = Player.REPEAT_MODE_ALL
    }

    private fun loadVideo(url: String?) {
        if (url != null) {
            val mediaItem: MediaItem = MediaItem.fromUri(url)
            binding.videoPlayer.player?.setMediaItem(mediaItem)
            binding.videoPlayer.player?.prepare()
            binding.videoPlayer.player?.play()
        }

    }

    private fun loadListOfVideos(videos: List<VideoData>) {
        binding.videoList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = VideosAdapter(videos) {
            loadVideo(it?.fileUrl)
            binding.videoContainer.removeViews(
                1,
                binding.videoContainer.childCount - 1
            ) // remove all movable edittexts
        }
        binding.videoList.adapter = adapter
        loadVideo(videos.first().fileUrl)
    }

    sealed class MainScreenState {
        object Loading : MainScreenState()
        class Success(val videos: List<VideoData>) : MainScreenState()
        class Error(val message: String?) : MainScreenState()
    }

}