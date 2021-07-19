package com.example.khind.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.model.DetailSensor
import com.example.khind.model.ResponseSensor
import com.example.khind.model.Sensor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    private val retroInstance : ApiService = RetroInstance.getRetroInstance().create(ApiService::class.java)
    var sensorsLiveData = MutableLiveData<ResponseSensor>()
    var detailSensorLivedata = MutableLiveData<DetailSensor>()

    fun sensorsLiveDataObserver(): MutableLiveData<ResponseSensor>{
        return sensorsLiveData
    }

    fun detailSensorLivaDataObserver(): MutableLiveData<DetailSensor>{
        return detailSensorLivedata
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
                Log.d("call api fetch sensors failed",t.message.toString())
            }

        })
    }

    fun fetchDetailSensor(token: String, sensorID: String){
        val call = retroInstance.getDetailSensor(token,sensorID)
        call.enqueue(object : Callback<DetailSensor>{
            override fun onResponse(call: Call<DetailSensor>, response: Response<DetailSensor>) {
                if (response.code()==200) detailSensorLivedata.postValue(response.body())
                else detailSensorLivedata.postValue(null)
            }

            override fun onFailure(call: Call<DetailSensor>, t: Throwable) {
                detailSensorLivedata.postValue(null)
                Log.d("call api fetch detail sensor","failed")
            }

        })
    }

}