package com.example.khind.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.model.Sensor
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates

class MapFragment : Fragment() {

    var isCall = false
    lateinit var nowSensor: Sensor
    lateinit var homeActivity: HomeActivity
    var expired by Delegates.notNull<Long>()
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = "Dashboard"
        homeActivity = activity as HomeActivity
        homeViewModel = homeActivity.getViewModelHome()
        loginViewModel = homeActivity.getViewModelLogin()
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val img = view.findViewById<ImageView>(R.id.idStatusTextFr)
        if (homeViewModel.getNowSensorData()?.alarm == "clear") img.setImageResource(R.drawable.allclear)
        val supportMapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.idMap) as SupportMapFragment
        supportMapFragment.getMapAsync {
            homeViewModel.setMapData(it)
        }
        makeObserver()

        expired = loginViewModel.getExpired()
        nowSensor = homeViewModel.getNowSensorData()!!

        if (expired * 1000 > System.currentTimeMillis()) homeViewModel.callAPIDetailSensor(
            loginViewModel.getTokenLogin(),
            nowSensor.id
        )
        else {
            isCall = true
            loginViewModel.callAPIRefreshToken(
                loginViewModel.getTokenLogin(),
                loginViewModel.getReTokenLogin()
            )
        }
        return view
    }

    private fun makeObserver() {
        homeViewModel.getDetailSensorLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null) {
                val lat = it.data.latitude
                val lon = it.data.longitude
                val title = it.data.display_name
                if (lat != null && lon != null) createMarker(lat, lon, title)
            }
        })

        loginViewModel.getReTokenLiveDataObserver().observe(viewLifecycleOwner, {
            if (it != null && isCall) {
                homeViewModel.callAPIDetailSensor(loginViewModel.getTokenLogin(), nowSensor.id)
                isCall = false
            }
        })
    }

    private fun createMarker(lat: Double, lon: Double, title: String) {
        val coordinates = LatLng(lat, lon)
        val marker = MarkerOptions().position(coordinates).title(title)
        drawCircle(coordinates, 90.0, Color.RED)
        drawCircle(coordinates, 120.0, Color.RED)
        drawCircle(coordinates, 150.0, Color.RED)
        homeViewModel.getMapData().addMarker(marker)
        homeViewModel.getMapData()
            .animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 17f), 2000, null)
    }

    private fun drawCircle(point: LatLng, radius: Double, color: Int) {
        val circleOptions = CircleOptions()
        circleOptions.apply {
            center(point)
            radius(radius)
            strokeColor(color)
            strokeWidth(3f)
            homeViewModel.getMapData().addCircle(circleOptions)
        }

    }

}
