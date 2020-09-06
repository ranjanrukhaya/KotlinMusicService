package com.gaura.kotlinmusicservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gaura.kotlinmusicservice.model.Track
import com.gaura.kotlinmusicservice.notification.MusicNotification
import com.gaura.kotlinmusicservice.service.MusicService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MusicPlayable {

    lateinit var notificationManager: NotificationManager

    private lateinit var tracks: ArrayList<Track>
    var position = 0
    var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        play.setOnClickListener {
            startService(Intent(this, MusicService::class.java))
            if (isPlaying) {
                onTrackPause()
            } else {
                onTrackPlay()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
            registerReceiver(broadcastReceiver, IntentFilter("TRACKS_TRACKS"))
        }

        setData();
    }

    private fun setData() {
        tracks = ArrayList<Track>()
        tracks.add(Track("Title1", "Artist1", R.drawable.music_bg1))
        tracks.add(Track("Title2", "Artist2", R.drawable.music_bg2))
        tracks.add(Track("Title3", "Artist3", R.drawable.music_bg3))
        tracks.add(Track("Title4", "Artist4", R.drawable.music_bg4))
    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MusicNotification.channel,
                "Gaura App",
                NotificationManager.IMPORTANCE_LOW
            )

            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.let {
                it.createNotificationChannel(channel)
            }
        }
    }

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.extras!!.getString("actionname")
            when (action) {
                MusicNotification.ACTION_PREVIUOS -> onTrackPrevious()
                MusicNotification.ACTION_PLAY -> if (isPlaying) {
                    onTrackPause()
                } else {
                    onTrackPlay()
                }
                MusicNotification.ACTION_NEXT -> onTrackNext()
            }
        }
    }

    override fun onTrackPrevious() {
        position--
        MusicNotification.createNotification(
            this,
            tracks[position],
            R.drawable.ic_pause,
            position,
            tracks.size - 1
        )
    }

    override fun onTrackPlay() {
        MusicNotification.createNotification(
            this,
            tracks[position],
            R.drawable.ic_pause,
            position,
            tracks.size - 1
        )
        play.setImageResource(R.drawable.ic_pause)
        isPlaying = true
        title_text.text = tracks[position].title
    }

    override fun onTrackPause() {
        MusicNotification.createNotification(
            this,
            tracks[position],
            R.drawable.ic_play,
            position,
            tracks.size - 1
        )
        play.setImageResource(R.drawable.ic_play)
        isPlaying = false
    }

    override fun onTrackNext() {
        position++
        MusicNotification.createNotification(
            this,
            tracks[position],
            R.drawable.ic_pause,
            position,
            tracks.size - 1
        )
        title_text.text = tracks[position].title
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll()
        }

        unregisterReceiver(broadcastReceiver)
    }
}