package com.example.connectinternet.navigation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.connectinternet.internet.ErrorScreen
import com.example.connectinternet.main.MainContent
import com.example.connectinternet.speech.AudioRecorder

@Composable
fun NavHost(isOnline: Boolean,navController: NavHostController) {

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isOnline) {
                NavHost(navController = navController, startDestination = "Main") {
                    composable("Main") {
                        MainContent(navController)
                    }
                    composable("Speech") {
                        AudioRecorder()
                    }
                }
            } else {
                ErrorScreen()
            }
        }
    }
}