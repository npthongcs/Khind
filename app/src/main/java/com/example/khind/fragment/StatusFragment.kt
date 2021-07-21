package com.example.khind.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.model.Sensor
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel
import kotlin.properties.Delegates

class StatusFragment : Fragment(R.layout.fragment_status) {

    var isCall = false
    lateinit var reToken: String
    private lateinit var token: String
    lateinit var imgStatusText: ImageView
    private lateinit var imgStatus: ImageView
    var expired by Delegates.notNull<Long>()
    private lateinit var homeActivity: HomeActivity
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var homeViewModel: HomeViewModel

    private fun makeObserver() {
        homeViewModel.getSensorsLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                if (homeViewModel.getNowSensorData() == null) homeViewModel.setNowSensorData(it.data[0])
                setState()
            }
        })

        loginViewModel.getReTokenLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null && isCall) {
                homeViewModel.callAPISensors(loginViewModel.getTokenLogin())
                isCall = false
            }
        })
    }

    private fun setState() {
        val sensor = homeViewModel.getNowSensorData()
        homeActivity.setNameAddress()
        when (sensor?.alarm) {
            "clear" -> {
                imgStatus.setImageResource(R.drawable.green_status)
                imgStatusText.setImageResource(R.drawable.allclear)
            }
            "warning" -> imgStatus.setImageResource(R.drawable.orange_status)
            "alert" -> imgStatus.setImageResource(R.drawable.red_status)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_status, container, false)

        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = "Dashboard"

        homeActivity = activity as HomeActivity
        loginViewModel = homeActivity.getViewModelLogin()
        homeViewModel = homeActivity.getViewModelHome()

        token = loginViewModel.getTokenLogin()
        reToken = loginViewModel.getReTokenLogin()
        expired = loginViewModel.getExpired()

        makeObserver()
        if (expired * 1000 > System.currentTimeMillis()) homeViewModel.callAPISensors(token)
        else {
            isCall = true
            loginViewModel.callAPIRefreshToken(token, reToken)
        }
        imgStatus = view.findViewById(R.id.imgStatus)
        imgStatusText = view.findViewById(R.id.idStatusText)
        return view
    }

}