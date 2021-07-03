package com.mateusandreatta.helpfilmes.network

import java.io.IOException

sealed class NetworkResponse <out T : Any, out U : Any> {
    data class Success<T : Any>(val body : T) : NetworkResponse<T, Nothing>()
    data class ApiError<U : Any>(val body : U, val code : Int) : NetworkResponse<Nothing, U>()
    data class NetworkError(val error : IOException) : NetworkResponse<Nothing, Nothing>()
    data class UnknowError(val error: Throwable? = null) : NetworkResponse<Nothing, Nothing>()
}