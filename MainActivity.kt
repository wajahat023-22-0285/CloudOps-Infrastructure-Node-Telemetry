package com.example.myapplication

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : ComponentActivity() {

    private val channelId = "enterprise_monitoring_channel"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Main Layout Container Setup (Programmatic UI)
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(60, 80, 60, 80)
            setBackgroundColor(Color.parseColor("#0F172A")) // Premium Dark Theme
        }

        // App Title Header
        val headerTitle = TextView(this).apply {
            text = "Cloud-Ops Infrastructure Node"
            textSize = 22f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 80)
        }
        mainLayout.addView(headerTitle)

        // --- BUTTON 1: CRITICAL ALERT ---
        val btnCritical = Button(this).apply {
            text = "TRIGGER CRITICAL CORE ALERT"
            setBackgroundColor(Color.parseColor("#EF4444")) // Crimson Red
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 40)
            }
        }
        btnCritical.setOnClickListener { triggerNotification(1, "🚨 SYSTEM BREACH SIMULATION", "Unauthorized database access attempt detected on Node-4.", android.R.drawable.ic_dialog_alert) }
        mainLayout.addView(btnCritical)

        // --- BUTTON 2: DATABASE SYNC ---
        val btnSync = Button(this).apply {
            text = "RUN DATABASE BACKUP SYNC"
            setBackgroundColor(Color.parseColor("#10B981")) // Emerald Green
            setTextColor(Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 40)
            }
        }
        btnSync.setOnClickListener { triggerNotification(2, "✅ CLOUD SYNC COMPLETE", "Local relational state engine compiled and pushed to remote AWS instances.", android.R.drawable.ic_menu_save) }
        mainLayout.addView(btnSync)

        // --- BUTTON 3: NETWORK DIAGNOSTICS ---
        val btnNetwork = Button(this).apply {
            text = "DIAGNOSE NETWORK LATENCY"
            setBackgroundColor(Color.parseColor("#F59E0B")) // Amber Yellow
            setTextColor(Color.BLACK)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
        btnNetwork.setOnClickListener { triggerNotification(3, "⚠️ LATENCY WARNING", "Server response delayed by +140ms. Scaling backup nodes active.", android.R.drawable.stat_sys_warning) }
        mainLayout.addView(btnNetwork)

        setContentView(mainLayout)

        // Initialize Native Channels
        createNotificationChannel()

        // Android 13+ Runtime Permissions Guard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun triggerNotification(id: Int, title: String, message: String, iconRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Required!", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(this).notify(id, builder.build())
        } catch (e: SecurityException) {
            Toast.makeText(this, "Security Execution Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Enterprise DevOps Monitor"
            val descriptionText = "Channel for cluster simulations and operations telemetry updates"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}