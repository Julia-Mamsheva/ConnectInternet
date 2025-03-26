package com.example.connectinternet.speech

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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

    fun uploadAudioFile(filePath: String) {
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
                    _resultText.value = "Error: ${e.message}"
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        _resultText.value =  Gson().fromJson(response.body?.string(), Result::class.java).result
                        Log.d("resultText",  _resultText.value)
                    } else {
                        _resultText.value = "Ошибка: ${response.code}, ${response.message}"
                        Log.d("resultText",  _resultText.value)
                    }
                }
            })
        }
    }


}
