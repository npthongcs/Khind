package com.example.khind.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khind.model.DetailSensor
import com.example.khind.model.ResponseSensor
import com.example.khind.model.Sensor
import com.example.khind.repository.HomeRepository
import com.google.android.gms.maps.GoogleMap

class HomeViewModel : ViewModel() {
    private val mHomeRepository = HomeRepository()
    var nowSensor: Sensor? = null
    lateinit var map: GoogleMap

    fun setMapData(data: GoogleMap) {map = data}
    fun getMapData(): GoogleMap {return map}

    fun setNowSensorData(data: Sensor) { nowSensor = data }
    fun getNowSensorData(): Sensor? = nowSensor

    fun callAPISensors(token: String) { mHomeRepository.fetchSensors(token) }
    fun callAPIDetailSensor(token: String, sensorID: String) { mHomeRepository.fetchDetailSensor(token, sensorID) }

    fun getSensorsLiveDataObserver(): MutableLiveData<ResponseSensor> {
        return mHomeRepository.sensorsLiveDataObserver()
    }
    fun getDetailSensorLiveDataObserver(): MutableLiveData<DetailSensor>{
        return mHomeRepository.detailSensorLivaDataObserver()
    }
}