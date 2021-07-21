package com.example.khind.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.example.khind.R
import com.example.khind.databinding.ActivityHomeBinding
import com.example.khind.fragment.*
import com.example.khind.model.Data
import com.example.khind.model.Sensor
import com.example.khind.model.Token
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlin.math.log
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity(R.layout.fragment_fag) {

    companion object {
        lateinit var fragmentManager: FragmentManager
        var loginViewModel = LoginViewModel()
        var homeViewModel = HomeViewModel()
    }

    var email: String = ""
    var avatar: String? = null
    private lateinit var token: String
    private lateinit var reToken: String
    private lateinit var txtAddress: TextView
    private var expired by Delegates.notNull<Long>()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        txtAddress = binding.nowSensor
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        HomeActivity.fragmentManager = supportFragmentManager

        val user: Data = intent.getSerializableExtra("user") as Data
        token = user.token.token
        reToken = user.token.refresh_token
        expired = user.token.expired_at
        email = user.email
        avatar = user.avatar
        loginViewModel.setData(token, reToken, expired, email, avatar)

        // set navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.statusFragment,
                R.id.mapFragment,
                R.id.historyFragment,
                R.id.profileFragment,
                R.id.settingFragment
            ),
            binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)

        binding.nowSensor.setOnClickListener {
            navController.navigateUp()
            navController.navigate(R.id.changeLocationFragment)
        }
        binding.imgChangeLo.setOnClickListener {
            navController.navigateUp()
            navController.navigate(R.id.statusFragment)
        }
        makeObserver()
    }

    private fun makeObserver() {
        loginViewModel.getReTokenLiveDataObserver().observe(this, {
            if (it != null) {
                token = it.data.token.token
                reToken = it.data.token.refresh_token
                expired = it.data.token.expired_at
                loginViewModel.setData(token, reToken, expired, email, avatar)
            }
        })
    }

    fun getViewModelLogin(): LoginViewModel {
        return loginViewModel
    }

    fun getViewModelHome(): HomeViewModel {
        return homeViewModel
    }

    fun setNameAddress() {
        txtAddress.text = homeViewModel.getNowSensorData()?.display_name ?: ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}