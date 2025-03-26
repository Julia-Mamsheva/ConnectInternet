package com.example.connectinternet.speach

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.connectinternet.API.ApiService
import com.example.connectinternet.API.Result
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

import java.io.IOException

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.File

class SpeechViewModel : ViewModel() {
    private val client = OkHttpClient()
    private var resultText by mutableStateOf("")
    fun uploadAudioFile(filePath: String) {
        viewModelScope.launch {
            val file = File(filePath)
            val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
            val request = Request.Builder()
                .url("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize?topic=general")
                .addHeader("Authorization", "Api-key AQVN3SBzhU-ix0y0-vo6WfrEXmlo7f04h_lwVFek")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    resultText = "Error: ${e.message}"
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val jsonResponse = response.body?.string()
                        resultText = jsonResponse ?: "Нет результата"
                        Log.d("resultText",resultText)
                    } else {
                        resultText = "Ошибка: ${response.code}, ${response.message}"
                        Log.d("resultText",resultText)
                    }
                }
            })
        }
    }


}
