package ru.spiritblog.ballontapper

import android.app.Activity
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log


private const val TAG = "Audio"


class Audio(activity: Activity) {

    private var soundId = 0
    private lateinit var mplayer: MediaPlayer
    private var volume: Float = 0F
    private var soundPool: SoundPool
    private var isLoaded = false

    init {
        val audioManager: AudioManager = activity.getSystemService(Context.AUDIO_SERVICE)
                as AudioManager
        val actVolume: Float = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
        volume = actVolume / maxVolume

        activity.volumeControlStream = AudioManager.STREAM_MUSIC

        val audioAttrib: AudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttrib)
            .setMaxStreams(6)
            .build()


        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            Log.d(TAG, "SoundPool is loaded")
            isLoaded = true
        }

        soundId = soundPool.load(activity, R.raw.pop, 1)

    }


    fun playSound() {
        if (isLoaded) {
            soundPool.play(soundId, volume, volume, 1, 0, 1f)
        }

        Log.d(TAG, "playSound")

    }


    fun prepareMediaPlayer(ctx: Context) {
        mplayer = MediaPlayer.create(ctx.applicationContext, R.raw.ngoni)
        mplayer.setVolume(0.5F, 0.5F)
        mplayer.isLooping = true
    }

    fun playMusic() {
        mplayer.start()
    }


    fun stopMusic() {
        mplayer.stop()
    }

    fun pauseMusic() {
        mplayer.pause()
    }


}