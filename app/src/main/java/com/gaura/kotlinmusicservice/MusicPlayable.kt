package com.gaura.kotlinmusicservice

interface MusicPlayable {
    fun onTrackPrevious()
    fun onTrackPlay()
    fun onTrackPause()
    fun onTrackNext()
}