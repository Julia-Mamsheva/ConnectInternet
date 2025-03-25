package com.example.connectinternet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
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

            if (isOnline) {
                MainContent()
            } else {
                ErrorScreen()
            }
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

@Composable
fun MainContent() {
    // Ваш основной контент приложения
    Text(text = "Добро пожаловать в приложение!")
}
