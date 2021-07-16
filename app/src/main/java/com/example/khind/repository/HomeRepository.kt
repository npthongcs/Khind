package com.example.khind.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.model.ResponseSensor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    private val retroInstance : ApiService = RetroInstance.getRetroInstance().create(ApiService::class.java)
    var sensorsLiveData = MutableLiveData<ResponseSensor>()

    fun sensorsLiveDataObserver(): MutableLiveData<ResponseSensor>{
        return sensorsLiveData
    }

    fun fetchSensors(token: String){
        val call = retroInstance.getSensors(token)
        call.enqueue(object : Callback<ResponseSensor>{
            override fun onResponse(
                call: Call<ResponseSensor>,
                response: Response<ResponseSensor>
            ) {
                Log.d("call api","sensor")
                if (response.code()==200) sensorsLiveData.postValue(response.body())
                else sensorsLiveData.postValue(null)
            }

            override fun onFailure(call: Call<ResponseSensor>, t: Throwable) {
                sensorsLiveData.postValue(null)
                Log.d("call api fetch sensors","failed")
            }

        })
    }

}