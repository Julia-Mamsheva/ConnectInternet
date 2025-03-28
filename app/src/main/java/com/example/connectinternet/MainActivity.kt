package com.example.connectinternet

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.connectinternet.internet.NetworkStatus
import com.example.connectinternet.navigation.NavHost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var networkStatus: NetworkStatus
    private val _isOnline = MutableStateFlow(false)
    val isOnline: StateFlow<Boolean> = _isOnline


    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkStatus = NetworkStatus(this)
        _isOnline.value = networkStatus.isOnline()

        NotificationService.createNotificationChannel(this)

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS)
            }
        }


        setContent {
            val isOnline by isOnline.collectAsState()
            val navController = rememberNavController()

             NavHost(isOnline, navController)
        }

        lifecycleScope.launch {
            while (true) {
                _isOnline.value = networkStatus.isOnline()
                kotlinx.coroutines.delay(2000) // Проверка каждые 2 секунды
            }
        }

    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Log.d("requestPermissionLauncher", "Not granted")
            }
        }

    override fun onResume() {
        super.onResume()

        alarmMgr?.cancel(alarmIntent)
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    override fun onStop() {
        super.onStop()

        setAlarmAfterMinute()
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun setAlarmAfterMinute() {
        alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, AlarmBroadcastReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        alarmMgr?.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 10 * 1000,
            alarmIntent
        )
    }
}

