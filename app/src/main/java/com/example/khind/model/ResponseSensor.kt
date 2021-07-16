package com.example.khind.model

data class ResponseSensor(val data: ArrayList<Sensor>, val status: Boolean)

data class DetailSensor(val data: Sensor, val status: Boolean)

data class Sensor(
    val id: String,
    val display_name: String,
    val alarm: String,
    val latitude: String?,
    val longitude: String?
)
