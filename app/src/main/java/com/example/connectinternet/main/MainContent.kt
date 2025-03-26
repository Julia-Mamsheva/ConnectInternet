package com.example.connectinternet.main

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale


@Composable
fun MainContent() {
    // Ваш основной контент приложения
    Text(text = "Добро пожаловать в приложение! \uD83D\uDE1E")
}

@Composable
fun LanguageSelectionScreen() {
    var selectedLanguage by remember { mutableStateOf("English") }
    var showDialog by remember { mutableStateOf(false) }

    // Список доступных языков
    val languages = listOf("Español", "Français", "Deutsch","\uD83D\uDE1E")

    // Проверка системного языка
    val systemLanguage = Locale.getDefault().displayLanguage
    Log.d("language", systemLanguage)
    val availableLanguages = listOf(systemLanguage, "English", "Español", "Français", "Deutsch")


    // Основной интерфейс
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Выберите язык", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text(text = "Выбрать язык: $selectedLanguage")
        }

        if (showDialog) {
            LanguageSelectionDialog(
                availableLanguages = availableLanguages,
                onDismiss = { showDialog = false },
                onLanguageSelected = { language ->
                    selectedLanguage = language
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    availableLanguages: List<String>,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Выберите язык") },
        text = {
            Column {
                availableLanguages.forEach { language ->
                    TextButton(onClick = { onLanguageSelected(language) }) {
                        Text(text = language)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}
