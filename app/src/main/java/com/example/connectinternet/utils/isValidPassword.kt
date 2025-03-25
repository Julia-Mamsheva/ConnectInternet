package com.example.connectinternet.utils

import android.text.TextUtils

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && Regex("^[a-z0-9]+@[a-z0-9]+\\.ru$").matches(this)
}

fun String.isPasswordValid(): Boolean {
    return Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$").matches(this)
}