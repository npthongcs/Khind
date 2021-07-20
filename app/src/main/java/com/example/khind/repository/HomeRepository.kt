package com.example.khind.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeRepository {
    private val retroInstance : ApiService = RetroInstance.getRetroInstance().create(ApiService::class.java)
    var sensorsLiveData = MutableLiveData<ResponseSensor>()
    var detailSensorLivedata = MutableLiveData<DetailSensor>()
    var changePassLiveData = MutableLiveData<ResponseChangePass>()
    var changeAvatarLiveData = MutableLiveData<ResponseChangeAvatar>()

    fun sensorsLiveDataObserver(): MutableLiveData<ResponseSensor>{
        return sensorsLiveData
    }

    fun detailSensorLivaDataObserver(): MutableLiveData<DetailSensor>{
        return detailSensorLivedata
    }

    fun changePassLiveDataObserver(): MutableLiveData<ResponseChangePass>{
        return changePassLiveData
    }

    fun changeAvatarLiveDataObserver(): MutableLiveData<ResponseChangeAvatar>{
        return changeAvatarLiveData
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

    fun changePassword(token: String, password: String, confirmPass: String, currentPass: String){
        val call = retroInstance.changePassword(token, password, confirmPass, currentPass)
        call.enqueue(object : Callback<ResponseChangePass>{
            override fun onResponse(
                call: Call<ResponseChangePass>,
                response: Response<ResponseChangePass>
            ) {
                if (response.isSuccessful) changePassLiveData.postValue(response.body())
                else {
                    val responseChangePass = ResponseChangePass("",false)
                    changePassLiveData.postValue(responseChangePass)
                }
            }

            override fun onFailure(call: Call<ResponseChangePass>, t: Throwable) {
                Log.d("call api change password","failed")
                changePassLiveData.postValue(null)
            }

        })
    }

    fun changeAvatar(token: String, avt: MultipartBody.Part){
        val call = retroInstance.changeAvatar(token, avt)
        call.enqueue(object : Callback<ResponseChangeAvatar>{
            override fun onResponse(
                call: Call<ResponseChangeAvatar>,
                response: Response<ResponseChangeAvatar>
            ) {
                if (response.isSuccessful) changeAvatarLiveData.postValue(response.body())
                else changeAvatarLiveData.postValue(null)
            }

            override fun onFailure(call: Call<ResponseChangeAvatar>, t: Throwable) {
                changeAvatarLiveData.postValue(null)
                Log.d("call api change avatar","failed")
            }

        })
    }

}