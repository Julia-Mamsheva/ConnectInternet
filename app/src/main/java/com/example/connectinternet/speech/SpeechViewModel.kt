package com.example.connectinternet.speech

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

import java.io.IOException

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.*
import java.io.File

class SpeechViewModel : ViewModel() {
    private val client = OkHttpClient()
    private val _resultText = MutableStateFlow<String>("")
    val resultText: StateFlow<String> = _resultText.asStateFlow()

    private val _resultState = MutableStateFlow<ResultState>(ResultState.Initialized)
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()


    fun uploadAudioFile(filePath: String) {
        _resultState.value = ResultState.Loading
        viewModelScope.launch {
            val file = File(filePath)
            val requestBody =
                RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
            val request = Request.Builder()
                .url("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize?topic=general&lang=en-US")
                .addHeader("Authorization", "Api-key AQVN3SBzhU-ix0y0-vo6WfrEXmlo7f04h_lwVFek")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    ResultState.Error(e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        _resultText.value =  Gson().fromJson(response.body?.string(), Result::class.java).result
                        Log.d("resultText",  _resultText.value)
                        _resultState.value = ResultState.Success(_resultText.value)
                    } else {
                        _resultText.value = "Ошибка: ${response.code}, ${response.message}"
                        Log.d("resultText",  _resultText.value)
                        _resultState.value = ResultState.Error(_resultText.value)

                    }
                }
            })
        }
    }
    fun uploadAudioFileRussia(filePath: String) {
        _resultState.value = ResultState.Loading
        viewModelScope.launch {
            val file = File(filePath)
            val requestBody =
                RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
            val request = Request.Builder()
                .url("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize?topic=general&lang=ru-Ru")
                .addHeader("Authorization", "Api-key AQVN3SBzhU-ix0y0-vo6WfrEXmlo7f04h_lwVFek")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    ResultState.Error(e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        _resultText.value =  Gson().fromJson(response.body?.string(), Result::class.java).result
                        Log.d("resultText",  _resultText.value)
                        _resultState.value = ResultState.Success(_resultText.value)
                    } else {
                        _resultText.value = "Ошибка: ${response.code}, ${response.message}"
                        Log.d("resultText",  _resultText.value)
                        _resultState.value = ResultState.Error(_resultText.value)
                    }
                }
            })
        }
    }
    fun startRecording(context: Context, recorder: MediaRecorder, onRecordingStarted: (String) -> Unit) {
        _resultState.value = ResultState.Loading
        val outputFile = File(context.getExternalFilesDir(null), "audio.ogg")
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.OGG)
            setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
            setOutputFile(outputFile.absolutePath)
            try {
                prepare()
                start()
                onRecordingStarted(outputFile.absolutePath)
                Toast.makeText(context, "Запись начала", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("startRecording", e.message.toString())

                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun stopRecording(context: Context, recorder: MediaRecorder) {
        _resultState.value = ResultState.Initialized
        recorder.apply {
            try {
                stop()
                release()
                Toast.makeText(context, "Запись успешно завершена", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("stopRecording", e.message.toString())
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }


}
