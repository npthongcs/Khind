package com.example.khind.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.manager.RequestManagerFragment
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.model.RefreshToken
import com.example.khind.model.ResponseLogin
import com.example.khind.model.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginRepository {
    private var reTokenLiveData = MutableLiveData<RefreshToken>()
    var loginLiveData = MutableLiveData<ResponseLogin>()
    private val retroInstance: ApiService = RetroInstance.getRetroInstance().create(ApiService::class.java)

    fun reTokenLiveDataObserver(): MutableLiveData<RefreshToken>{
        return reTokenLiveData
    }

    fun loginLiveDataObserver(): MutableLiveData<ResponseLogin>{
        return loginLiveData
    }

    fun fetchLogin(email: String, password: String){
        val call = retroInstance.login(email, password)
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.code()==200) loginLiveData.postValue(response.body())
                else loginLiveData.postValue(null)
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d("call api login","failed")
            }

        })
    }

    fun fetchRefreshToken(token: String, reToken: String){
        val call = retroInstance.refreshToken(token,reToken)
        call.enqueue(object : Callback<RefreshToken>{
            override fun onResponse(call: Call<RefreshToken>, response: Response<RefreshToken>) {
                if (response.code()==200) reTokenLiveData.postValue(response.body())
                else reTokenLiveData.postValue(null)
            }

            override fun onFailure(call: Call<RefreshToken>, t: Throwable) {
                reTokenLiveData.postValue(null)
                Log.d("call api refresh token","failed")
            }

        })
    }
}