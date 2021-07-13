package com.example.khind.`interface`

import com.example.khind.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("auth/sign_in")
    fun login(
        @Query("email") email:String,
        @Query("password") password:String
    ): Call<ResponseLogin>
}