package com.example.khind.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.databinding.FragmentDetailMessageBinding
import com.example.khind.model.Message
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class DetailMessageFragment : Fragment(R.layout.fragment_detail_message) {

    private val args: DetailMessageFragmentArgs by navArgs()
    private var _binding: FragmentDetailMessageBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title=""
        titleToolBar?.text = "Details"
        // Inflate the layout for this fragment
        _binding = FragmentDetailMessageBinding.inflate(inflater, container, false)
        val date = Date(args.data.created_at)
        val format = SimpleDateFormat("dd MMM, hh:mma")
        binding.dateMess.text = format.format(date)
        binding.titleMess.text = args.data.title
        binding.contentMess.text = args.data.title
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
