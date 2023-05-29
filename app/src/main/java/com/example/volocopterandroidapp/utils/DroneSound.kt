package com.example.volocopterandroidapp.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer

class DroneSound(private val context: Context, private val soundResourceId: Int) {
    private var mediaPlayer: MediaPlayer? = null
    private var originalVolume: Float = 0.0f
    fun start() {

        mediaPlayer = MediaPlayer.create(context, soundResourceId)
        mediaPlayer?.isLooping = true

        //Set volume low of media player
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()

        // Set the volume to a low level (e.g., 25% of the maximum volume)
        val targetVolume = originalVolume * 0.25f
        mediaPlayer?.setVolume(targetVolume, targetVolume)

        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}