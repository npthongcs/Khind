package com.example.khind.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.model.ResponseMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotifyRepository {
    private val retroInstance: ApiService = RetroInstance.getRetroInstance().create(ApiService::class.java)
    var messagesLiveData = MutableLiveData<ResponseMessage>()

    fun messagesLiveDataObserver(): MutableLiveData<ResponseMessage>{
        return messagesLiveData
    }

    fun fetchMessages(token: String, page: Int){
        val call = retroInstance.getMessage(token, page)
        call.enqueue(object : Callback<ResponseMessage>{
            override fun onResponse(
                call: Call<ResponseMessage>,
                response: Response<ResponseMessage>
            ) {
                if (response.isSuccessful) messagesLiveData.postValue(response.body())
                else messagesLiveData.postValue(null)
            }

            override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                messagesLiveData.postValue(null)
                Log.d("call api get messages","failed")
            }

        })
    }
}