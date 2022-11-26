package com.bbeHumble.media.player.ui.videosList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bbeHumble.media.player.R
import com.bbeHumble.media.player.data.model.VideoData
import com.bumptech.glide.Glide

class VideosAdapter(
    val data: List<VideoData>,
    private val onSelect: (VideoData?) -> Unit
) : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.li_poster, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoData = data[position]
        Glide.with(holder.thumbnail)
            .load(videoData.thumbnailUrl)
            .centerCrop()
            .into(holder.thumbnail)
        holder.itemView.setOnClickListener {
            onSelect(videoData)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}

