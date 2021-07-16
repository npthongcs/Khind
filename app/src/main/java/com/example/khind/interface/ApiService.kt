package com.example.khind.`interface`

import com.example.khind.model.RefreshToken
import com.example.khind.model.ResponseLogin
import com.example.khind.model.ResponseSensor
import com.example.khind.model.Token
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/sign_in")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<ResponseLogin>

    @GET("sensors")
    fun getSensors(@Header("X-Http-Token") token: String): Call<ResponseSensor>

    @POST("auth/refresh_token")
    fun refreshToken(
        @Header("X-Refresh-Token") token: String,
        @Query("refresh_token") reToken: String
    ) : Call<RefreshToken>
}