package com.example.connectinternet.speech

data class Result(
    val result:String
)

sealed class ResultState {
    data object Loading : ResultState() // Это объект, представляющий состояние загрузки
    data object Initialized : ResultState() //  Это объект, представляющий состояние инициализации
    data class Success(val message: String) : ResultState() //  Это класс, представляющий успешное завершение операции.  Он содержит свойство message типа String, которое может содержать дополнительную информацию о результате
    data class Error(val message: String) : ResultState() //  Это класс, представляющий ошибку.  Он также содержит свойство message типа String,  которое описывает произошедшую ошибку
}