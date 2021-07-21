package com.example.khind.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class FagFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = "FAQ"
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fag, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onResume() {
        super.onResume()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val location = homeActivity.findViewById<ConstraintLayout>(R.id.idChangeLocation)
        location.visibility = View.GONE
        botBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val homeActivity = activity as HomeActivity
        val botBar = homeActivity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val location = homeActivity.findViewById<ConstraintLayout>(R.id.idChangeLocation)
        location.visibility = View.VISIBLE
        botBar.visibility = View.VISIBLE
    }
}