package com.example.khind.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khind.model.*
import com.example.khind.repository.HomeRepository
import com.google.android.gms.maps.GoogleMap
import okhttp3.MultipartBody

class HomeViewModel : ViewModel() {
    private val mHomeRepository = HomeRepository()
    var nowSensor: Sensor? = null
    private lateinit var map: GoogleMap

    fun setMapData(data: GoogleMap) {map = data}
    fun getMapData(): GoogleMap {return map}

    fun setNowSensorData(data: Sensor) { nowSensor = data }
    fun getNowSensorData(): Sensor? = nowSensor

    fun callAPISensors(token: String) { mHomeRepository.fetchSensors(token) }
    fun callAPIDetailSensor(token: String, sensorID: String) { mHomeRepository.fetchDetailSensor(token, sensorID) }
    fun callAPIChangePass(token: String, password: String, confirmPass: String, currentPass: String) {
        mHomeRepository.changePassword(token, password, confirmPass, currentPass)
    }
    fun callAPIChangeAvatar(token: String, avt: MultipartBody.Part){
        mHomeRepository.changeAvatar(token, avt)
    }

    fun getSensorsLiveDataObserver(): MutableLiveData<ResponseSensor> {
        return mHomeRepository.sensorsLiveDataObserver()
    }
    fun getDetailSensorLiveDataObserver(): MutableLiveData<DetailSensor>{
        return mHomeRepository.detailSensorLivaDataObserver()
    }
    fun getChangePassLiveDataObserver(): MutableLiveData<ResponseChangePass>{
        return mHomeRepository.changePassLiveDataObserver()
    }
    fun getChangeAvatarLiveDataObserver(): MutableLiveData<ResponseChangeAvatar>{
        return mHomeRepository.changeAvatarLiveDataObserver()
    }
}