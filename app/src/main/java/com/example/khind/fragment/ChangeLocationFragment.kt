package com.example.khind.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.adapter.LocationAdapter
import com.example.khind.listener.LocationOnClickListener
import com.example.khind.model.Sensor
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChangeLocationFragment : Fragment(), LocationOnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_change_location, container, false)
//        val rvChangeLocation = view.findViewById<RecyclerView>(R.id.rvChangeLocation)
//        val homeActivity = activity as HomeActivity
//        val homeViewModel = homeActivity.getViewModelHome()
//        homeViewModel.getSensorsLiveDataObserver().observe(viewLifecycleOwner, {
//            val locationAdapter = LocationAdapter(it.data)
//            locationAdapter.setOnCallBackListener(this)
//            rvChangeLocation.apply {
//                setHasFixedSize(true)
//                layoutManager = LinearLayoutManager(view.context)
//                adapter = locationAdapter
//            }
//
//        })
        return view
    }
//
//    override fun onResume() {
//        super.onResume()
//        val homeActivity = activity as HomeActivity
//        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
//        botBar.visibility = View.GONE
//        (activity as AppCompatActivity).supportActionBar?.hide()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        val homeActivity = activity as HomeActivity
//        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
//        botBar.visibility = View.VISIBLE
//        (activity as AppCompatActivity).supportActionBar?.show()
//    }
//
    override fun onItemClick(sensor: Sensor) {
//        val homeActivity = activity as HomeActivity
//        val homeViewModel = homeActivity.getViewModelHome()
//        homeViewModel.setNowSensorData(sensor)
//        val transaction = homeActivity.supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container, StatusFragment())
//        transaction.addToBackStack(null)
//        transaction.commit()
    }


}