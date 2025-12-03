package com.vero.app.data.remote

import com.vero.app.data.remote.TaskResponse
import com.vero.app.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("dev/index.php/login")
    suspend fun login(
        @Header("Authorization") authHeader: String,
        @Body credentials: Map<String, String>
    ): LoginResponse

    @GET("dev/index.php/v1/tasks/select")
    suspend fun getTasks(
        @Header("Authorization") token: String
    ): List<TaskResponse>
}
