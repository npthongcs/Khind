package com.example.khind.fragment

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text

class SetupScheduleFragment : Fragment() {

    private lateinit var startTime: TextView
    private lateinit var endTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setup_schedule, container, false)
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title = ""
        titleToolBar?.text = "Setup Schedule"

        startTime = view.findViewById(R.id.startTime)
        endTime = view.findViewById(R.id.endTime)
        startTime.setOnClickListener {
            selectStartTime()
        }
        endTime.setOnClickListener {
            selectEndTime()
        }
        return view
    }

    private fun selectEndTime() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
            val minu = if (minute < 10) "0$minute" else minute.toString()
            endTime.text = "$hour:$minu"
        }
        val timePickerDialog = TimePickerDialog(
            context, android.R.style.ThemeOverlay_Material_Dialog_Alert,
            timeSetListener, 9, 0, true
        )
        timePickerDialog.show()
    }

    private fun selectStartTime() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
            val minu = if (minute < 10) "0$minute" else minute.toString()
            startTime.text = "$hour:$minu"
        }
        val timePickerDialog = TimePickerDialog(
            context, android.R.style.ThemeOverlay_Material_Dialog_Alert,
            timeSetListener, 8, 0, true
        )
        timePickerDialog.show()
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