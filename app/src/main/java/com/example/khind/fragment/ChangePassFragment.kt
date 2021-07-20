package com.example.khind.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.properties.Delegates

class ChangePassFragment : Fragment(R.layout.fragment_change_pass) {

    private val loginViewModel = HomeActivity().getViewModelLogin()
    private val homeViewModel = HomeActivity().getViewModelHome()
    private var expired by Delegates.notNull<Long>()
    var isChange = false
    private var currentPass: String = ""
    private var newPass: String = ""
    private var confirmPass: String =""

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
        val view = inflater.inflate(R.layout.fragment_change_pass, container, false)
        val titleToolBar = activity?.findViewById<TextView>(R.id.titleToolbar)
        (activity as HomeActivity).supportActionBar?.title=""
        titleToolBar?.text = "Change Password"
        val btnChangePass = view.findViewById<Button>(R.id.btnChangePass)

        makeObserver()

        btnChangePass.setOnClickListener {
            currentPass = view.findViewById<EditText>(R.id.etOldPass).text.toString()
            newPass = view.findViewById<EditText>(R.id.etNewPass).text.toString()
            confirmPass = view.findViewById<EditText>(R.id.etConfirmPass).text.toString()

            if (currentPass=="" || newPass== "" || confirmPass== "" )
                Toast.makeText(context,"Old/new/confirm password is empty",Toast.LENGTH_SHORT).show()
            else if (newPass.length<6) Toast.makeText(context,"Password is too short (minimum is 6 characters)",Toast.LENGTH_SHORT).show()
            else if (newPass!=confirmPass) Toast.makeText(context,"Passwords doesn't match",Toast.LENGTH_SHORT).show()
            else {
                expired = loginViewModel.getExpired()
                if (expired * 1000 > System.currentTimeMillis()) homeViewModel.callAPIChangePass(
                    loginViewModel.getTokenLogin(),
                    newPass,
                    confirmPass,
                    currentPass
                ) else {
                    loginViewModel.callAPIRefreshToken(loginViewModel.getTokenLogin(),loginViewModel.getReTokenLogin())
                    isChange = true
                }
            }
        }
        return view
    }

    private fun makeObserver() {
        homeViewModel.getChangePassLiveDataObserver().observe(viewLifecycleOwner,{
            if (!it.status) Toast.makeText(context,"Current password is invalid",Toast.LENGTH_SHORT).show()
            else Toast.makeText(context,"Change password is successful",Toast.LENGTH_SHORT).show()
        })

        loginViewModel.getReTokenLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null && isChange){
                homeViewModel.callAPIChangePass(loginViewModel.getTokenLogin(),newPass,confirmPass,currentPass)
                isChange = false
            }
        })
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