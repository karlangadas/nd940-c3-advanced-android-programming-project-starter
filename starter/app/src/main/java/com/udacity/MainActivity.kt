package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.custom_button
import kotlinx.android.synthetic.main.content_main.download_options


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        createChannel(
            getString(R.string.load_app_notification_channel_id),
            getString(R.string.load_app_notification_channel_name)
        )
        custom_button.setOnClickListener {
            val radioButton: View? =
                download_options.findViewById(download_options.checkedRadioButtonId)
            if (radioButton == null) {
                Toast.makeText(
                    applicationContext,
                    "Please select the file to download",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            download(download_options.indexOfChild(radioButton))
        }
    }

    // adapted from https://stackoverflow.com/questions/32309637/set-extras-for-downloadmanagers-broadcastreceiver
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) ?: -1
            val query = DownloadManager.Query()
            query.setFilterById(downloadId)
            val cursor =
                (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).query(query)
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    val fileName =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION))
                    notificationManager.sendNotification(
                        getString(R.string.notification_description),
                        context,
                        fileName
                    )
                }
            }
        }
    }

    private fun download(selectedIndex: Int) {
        notificationManager.cancelNotifications()
        val request =
            DownloadManager.Request(Uri.parse(URLS[selectedIndex]))
                .setTitle(getString(R.string.app_name))
                .setDescription(REPO_NAMES[selectedIndex])
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                setShowBadge(false)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.enableVibration(true)
        notificationChannel.description =
            getString(R.string.load_app_notification_channel_description)

        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private val URLS =
            listOf(
                "https://github.com/bumptech/glide/archive/master.zip",
                "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
                "https://github.com/square/retrofit/archive/master.zip"
            )
        private val REPO_NAMES =
            listOf(
                "Glide - Image Loading Library by BumpTech",
                "ND940 C3 - Starter project repo for C3 by Udacity",
                "Retrofit - A type-safe HTTP client for Android and the JVM by Square"
            )
    }

}
