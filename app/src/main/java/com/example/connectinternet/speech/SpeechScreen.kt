package com.example.connectinternet.speech

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch
import java.io.File
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.IOException

@Composable
fun AudioRecorder(viewModel: SpeechViewModel = viewModel()) {
    val words = listOf(
        Pair("hello", "həˈloʊ"),
        Pair("world", "wɜrld"),
        Pair("compose", "kəmˈpoʊz"),
        Pair("android", "ˈændrɔɪd")
    )
    var cx = LocalContext.current

    val result = viewModel.resultText.collectAsState()
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var recordingFilePath by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    var recorder = remember { MediaRecorder() }
    var currentWord by remember { mutableStateOf(words.random()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = currentWord.first,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentWord.second,
                fontSize = 20.sp,
                color = Color.Gray
            )
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.RECORD_AUDIO),
                        1
                    )
                } else {
                    if (isRecording) {
                        viewModel.stopRecording(cx, recorder)
                    } else {
                        try {
                            recorder = MediaRecorder()
                            viewModel.startRecording(context, recorder) { path ->
                                recordingFilePath = path

                            }
                        } catch (e: IOException) {
                        }
                    }
                }
                isRecording = !isRecording
            }) {
                Text(if (isRecording) "Stop Recording" else "Start Recording")
            }
            Spacer(modifier = Modifier.height(16.dp))
            val resultState by viewModel.resultState.collectAsState()

            when (resultState) {
                is ResultState.Error -> {
                    recordingFilePath?.let { filePath ->
                        Button(onClick = {
                            scope.launch {
                                viewModel.uploadAudioFile(filePath)
                            }
                        }) {
                            Text("Upload Audio English")
                        }
                        Button(onClick = {
                            scope.launch {
                                viewModel.uploadAudioFileRussia(filePath)
                            }
                        }) {
                            Text("Upload Audio Russia")
                        }
                    }
                }

                ResultState.Initialized -> {
                    recordingFilePath?.let { filePath ->
                        Button(onClick = {
                            scope.launch {
                                viewModel.uploadAudioFile(filePath)
                            }
                        }) {
                            Text("Upload Audio English")
                        }
                        Button(onClick = {
                            scope.launch {
                                viewModel.uploadAudioFileRussia(filePath)
                            }
                        }) {
                            Text("Upload Audio Russia")
                        }
                    }
                }

                ResultState.Loading -> {
                    CircularProgressIndicator()
                }

                is ResultState.Success -> {
                    recordingFilePath?.let { filePath ->
                        Button(onClick = {
                            scope.launch {
                                viewModel.uploadAudioFile(filePath)
                            }
                        }) {
                            Text("Upload Audio English")
                        }
                        Button(onClick = {
                            scope.launch {
                                viewModel.uploadAudioFileRussia(filePath)
                            }
                        }) {
                            Text("Upload Audio Russia")
                        }
                    }
                }
            }



            Text(result.value)
        }
    }
}


