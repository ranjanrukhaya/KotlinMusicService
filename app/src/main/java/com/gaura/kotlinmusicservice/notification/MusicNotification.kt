package com.gaura.kotlinmusicservice.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gaura.kotlinmusicservice.R
import com.gaura.kotlinmusicservice.model.Track
import com.gaura.kotlinmusicservice.service.NotificationActionService


class MusicNotification {


    lateinit var notification: Notification


    companion object {
        val channel = "channel1"

        val ACTION_PREVIUOS = "actionprevious";
        val ACTION_PLAY = "actionplay";
        val ACTION_NEXT = "actionnext";

        fun createNotification(
            context: Context,
            track: Track,
            playButton: Int,
            pos: Int,
            size: Int
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManagerCompat = NotificationManagerCompat.from(context)
                val mediaSessionCompat = MediaSessionCompat(context, "tag")

                val icon =
                    BitmapFactory.decodeResource(
                        context.resources,
                        track.image
                    )


                var pendingIntentPrevious: PendingIntent?
                var drwPrevious = 0
                if (pos == 0) {
                    pendingIntentPrevious = null
                    drwPrevious = 0
                } else {
                    val intentPrevious =
                        Intent(context, NotificationActionService::class.java)
                            .setAction(ACTION_PREVIUOS)
                    pendingIntentPrevious = PendingIntent.getBroadcast(
                        context, 0,
                        intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    drwPrevious = R.drawable.ic_skip_previous_24
                }

                val intentPlay =
                    Intent(context, NotificationActionService::class.java).setAction(ACTION_PLAY)
                val pendingIntentPlay = PendingIntent.getBroadcast(
                    context, 0,
                    intentPlay, PendingIntent.FLAG_UPDATE_CURRENT
                )

                val pendingIntentNext: PendingIntent?
                val drwNext: Int
                if (pos == size) {
                    pendingIntentNext = null
                    drwNext = 0
                } else {
                    val intentNext = Intent(context, NotificationActionService::class.java)
                        .setAction(ACTION_NEXT)
                    pendingIntentNext = PendingIntent.getBroadcast(
                        context, 0,
                        intentNext, PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    drwNext = R.drawable.ic_skip_next_24
                }

                val notification = NotificationCompat.Builder(context, channel)
                    .setSmallIcon(R.drawable.ic_music_note_24)
                    .setContentText(track.artist)
                    .setContentTitle(track.title)
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(drwPrevious, "Previous", pendingIntentPrevious)
                    .addAction(playButton, "Play", pendingIntentPlay)
                    .addAction(drwNext, "Next", pendingIntentNext)
                    .setStyle(
                        androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.sessionToken)
                    )
                    .setPriority(Notification.PRIORITY_LOW)
                    .build()

                notificationManagerCompat.notify(1, notification)
            }
        }
    }
}