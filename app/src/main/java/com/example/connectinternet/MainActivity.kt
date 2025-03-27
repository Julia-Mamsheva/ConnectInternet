package com.example.connectinternet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkStatus = NetworkStatus(this)
        _isOnline.value = networkStatus.isOnline()

        setContent {
            val isOnline by isOnline.collectAsState()
            val navController = rememberNavController()

            NavHost(isOnline, navController)

        }
        // Проверка состояния сети
        lifecycleScope.launch {
            while (true) {
                _isOnline.value = networkStatus.isOnline()
                kotlinx.coroutines.delay(2000) // Проверка каждые 2 секунды
            }
        }
    }
}
/*
@Composable
fun InactivityNotification() {
    val context = LocalContext.current
    var lastAppOpenedTime by remember { mutableStateOf(0L) }
    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        lastAppOpenedTime = System.currentTimeMillis()

        scheduleWorkRequest(context)
    }


    LaunchedEffect(currentTime.value) {
        if (System.currentTimeMillis() - lastAppOpenedTime > 60000) {
            sendNotification(context)
            lastAppOpenedTime = System.currentTimeMillis()
            cancelWorkRequest(context)
            scheduleWorkRequest(context)
        }
    }
    LaunchedEffect(Unit){
        while (true){
            currentTime.value = System.currentTimeMillis()
            delay(1000)
        }
    }

}
fun scheduleWorkRequest(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<MyWorker>(1, TimeUnit.MINUTES)
        .setInitialDelay(1, TimeUnit.MINUTES)
        .addTag("inactivityNotification")
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "inactivityNotification",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )
}

fun cancelWorkRequest(context: Context) {
    WorkManager.getInstance(context).cancelUniqueWork("inactivityNotification")
}

@SuppressLint("MissingPermission")
fun sendNotification(context: Context) {
    createNotificationChannel(context)

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // Замените на свой иконку
        .setContentTitle("Уведомление о бездействии")
        .setContentText("Вы не использовали приложение более минуты")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
}


private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "InactivityNotificationChannel"
        val descriptionText = "Channel for inactivity notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
    sendNotification(applicationContext)
    return Result.success()
}
}


private const val CHANNEL_ID = "inactivity_notification_channel"
private const val NOTIFICATION_ID = 1*/
