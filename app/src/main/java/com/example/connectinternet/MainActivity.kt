package com.example.connectinternet


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.connectinternet.speach.AudioRecorder
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
             /*   MainContent()
                LanguageSelectionScreen()*/
                AudioRecorder()
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

