package com.example.khind.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.adapter.LocationAdapter
import com.example.khind.listener.LocationOnClickListener
import com.example.khind.model.Sensor
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChangeLocationFragment : Fragment(R.layout.fragment_change_location),
    LocationOnClickListener {

    var isCall = false
    lateinit var rvChangeLocation: RecyclerView
    lateinit var homeViewModel: HomeViewModel
    lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_location, container, false)
        rvChangeLocation = view.findViewById(R.id.rvChangeLocation)
        val homeActivity = activity as HomeActivity
        homeViewModel = homeActivity.getViewModelHome()
        loginViewModel = homeActivity.getViewModelLogin()

        makeObserver(view)
        val expired = loginViewModel.getExpired()
        if (expired * 1000 > System.currentTimeMillis()) homeViewModel.callAPISensors(loginViewModel.getTokenLogin())
        else {
            isCall = true
            loginViewModel.callAPIRefreshToken(
                loginViewModel.getTokenLogin(),
                loginViewModel.getReTokenLogin()
            )
        }
        return view
    }

    fun makeObserver(view: View) {
        homeViewModel.getSensorsLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                val locationAdapter = LocationAdapter(it.data)
                locationAdapter.setOnCallBackListener(this)
                rvChangeLocation.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = locationAdapter
                }
            }
        })

        loginViewModel.getReTokenLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null && isCall) {
                isCall = false
                homeViewModel.callAPISensors(loginViewModel.getTokenLogin())
            }
        })

    }

    override fun onResume() {
        super.onResume()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val titleToolBar = homeActivity.findViewById<TextView>(R.id.titleToolbar)
        titleToolBar.visibility = View.GONE
        botBar.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val titleToolBar = homeActivity.findViewById<TextView>(R.id.titleToolbar)
        titleToolBar.visibility = View.VISIBLE
        botBar.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    override fun onItemClick(sensor: Sensor) {
        val homeActivity = activity as HomeActivity
        val homeViewModel = homeActivity.getViewModelHome()
        homeViewModel.setNowSensorData(sensor)
        val action = ChangeLocationFragmentDirections.actionChangeLocationFragmentToStatusFragment()
        findNavController().navigate(action)
    }


}