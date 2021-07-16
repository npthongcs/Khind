package com.example.khind.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khind.model.ResponseSensor
import com.example.khind.model.Sensor
import com.example.khind.repository.HomeRepository

class HomeViewModel : ViewModel() {
    private val mHomeRepository = HomeRepository()
    var listSensor = ArrayList<Sensor>()
    var nowSensor: Sensor? = null

    fun setListSensorData(data: ArrayList<Sensor>){
        listSensor = data
    }

    fun getListSensorData(): ArrayList<Sensor>{
        return listSensor
    }

    fun setNowSensorData(data: Sensor){
        nowSensor = data
    }

    fun getNowSensorData(): Sensor?{
        return nowSensor
    }

    fun callAPISensors(token: String) = mHomeRepository.fetchSensors(token)

    fun getSensorsLiveDataObserver(): MutableLiveData<ResponseSensor> {
        return mHomeRepository.sensorsLiveDataObserver()
    }
}