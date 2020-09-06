package com.gaura.kotlinmusicservice

interface Payable {
    fun onTrackPrevious()
    fun onTrackPlay()
    fun onTrackPause()
    fun onTrackNext()
}