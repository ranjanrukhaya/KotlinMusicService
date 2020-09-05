package com.gaura.kotlinmusicservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        play_button.setOnClickListener {
            startService(Intent(this, MusicService::class.java))
        }

        stop_button.setOnClickListener {
            stopService(Intent(this, MusicService::class.java))
        }
    }
}