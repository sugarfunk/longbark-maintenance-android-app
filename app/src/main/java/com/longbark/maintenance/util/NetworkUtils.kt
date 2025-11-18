package com.longbark.maintenance.util

import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
            Result.Success(response.body()!!)
        } else {
            Result.Error(
                IOException("API Error: ${response.code()}"),
                response.message()
            )
        }
    } catch (e: Exception) {
        Result.Error(e, e.message)
    }
}
