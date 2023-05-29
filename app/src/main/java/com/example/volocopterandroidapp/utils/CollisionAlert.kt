package com.example.volocopterandroidapp.utils

import android.content.Context
import android.media.MediaPlayer

class CollisionAlert(private val context: Context, private val soundResourceId: Int) {
    var mediaPlayer: MediaPlayer? = null
    fun start() {
        mediaPlayer = MediaPlayer.create(context, soundResourceId)
        mediaPlayer?.start()
    }
    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}