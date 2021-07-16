package com.example.khind.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.model.Sensor
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel

class StatusFragment : Fragment() {

    private lateinit var token: String
    lateinit var reToken: String
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeActivity: HomeActivity
    private lateinit var imgStatus: ImageView
    lateinit var imgStatusText: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = activity as HomeActivity

        loginViewModel = homeActivity.getViewModelLogin()
        homeViewModel = homeActivity.getViewModelHome()

        token = loginViewModel.getTokenLogin()
        reToken = loginViewModel.getReTokenLogin()

        makeObserver()
    }

    private fun makeObserver() {
        homeViewModel.getSensorsLiveDataObserver().observe(this,{
            if (it!=null) setState()
        })
    }

    private fun setState() {
        val sensor = homeViewModel.getNowSensorData()
        Log.d("sensor","go go")
        if (sensor==null) homeViewModel.callAPISensors(token)
        else {
            when (sensor.alarm){
                "clear" -> {
                    imgStatus.setImageResource(R.drawable.green_status)
                    imgStatusText.visibility = View.GONE
                }
                "warning" -> imgStatus.setImageResource(R.drawable.orange_status)
                "alert" -> imgStatus.setImageResource(R.drawable.red_status)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_status, container, false)
        imgStatus = view.findViewById(R.id.imgStatus)
        imgStatusText = view.findViewById(R.id.idStatusText)
        return view
    }

}