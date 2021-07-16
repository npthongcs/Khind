package com.example.khind.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.khind.R
import com.example.khind.fragment.StatusFragment
import com.example.khind.model.Sensor
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlin.math.log


class HomeActivity : AppCompatActivity(){

    private lateinit var mDrawerLayout: DrawerLayout
    private var loginViewModel = LoginViewModel()
    private var homeViewModel = HomeViewModel()
    private lateinit var token: String
    private lateinit var reToken: String
    var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        token = intent.getStringExtra("token").toString()
        reToken = intent.getStringExtra("reToken").toString()
        loginViewModel.setToken(token,reToken)

        makeObserver()
        setupToolBar()
        setupMenuBar()
        setupBottomNavigation()

        homeViewModel.callAPISensors(token)
        val fr = StatusFragment()
        loadFragment(fr)
    }

    fun getViewModelLogin(): LoginViewModel {
        return this.loginViewModel
    }

    fun getViewModelHome(): HomeViewModel{
        return this.homeViewModel
    }

    private fun makeObserver() {
        loginViewModel.getReTokenLiveDataObserver().observe(this,{
            if (it!=null){
                token = it.data.token.token
                reToken = it.data.token.refresh_token
                loginViewModel.setToken(token, reToken)
                Log.d("refresh token home activity",token)
                when (type){
                    "GET_SENSOR" -> homeViewModel.callAPISensors(token)
                }
            }
        })

        homeViewModel.getSensorsLiveDataObserver().observe(this,{
            if (it!=null){
                homeViewModel.setListSensorData(it.data)
                homeViewModel.setNowSensorData(it.data[0])
            } else {
                type = "GET_SENSOR"
                loginViewModel.callAPIRefreshToken(token,reToken)
            }
        })
    }


    private fun setupBottomNavigation() {
        val botNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        botNav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.status -> {
                    title = "Status"
                    if (homeViewModel.getNowSensorData()==null) homeViewModel.callAPISensors(token)
                    loadFragment(StatusFragment())
                }
                R.id.mapview -> Toast.makeText(this,"map view",Toast.LENGTH_SHORT).show()
                R.id.history -> Toast.makeText(this,"history",Toast.LENGTH_SHORT).show()
                else -> false
            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setupToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dashboard"

        val styledAttributes =
            theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val actionBarSize = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()

        val drawable = ContextCompat.getDrawable(this,R.drawable.ic_menubar)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val newDrawable: Drawable = BitmapDrawable(
            resources,
            Bitmap.createScaledBitmap(bitmap, actionBarSize/2, actionBarSize/2, true)
        )
        supportActionBar?.setHomeAsUpIndicator(newDrawable)
    }

    private fun setupMenuBar() {
        mDrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.dashboard -> Toast.makeText(this,"dashboard",Toast.LENGTH_SHORT).show()
                R.id.notification -> Toast.makeText(this,"notification",Toast.LENGTH_SHORT).show()
                R.id.my_profile -> Toast.makeText(this,"my profile",Toast.LENGTH_SHORT).show()
                R.id.setting -> Toast.makeText(this,"setting",Toast.LENGTH_SHORT).show()
                R.id.support -> Toast.makeText(this,"support",Toast.LENGTH_SHORT).show()
            }
            mDrawerLayout.closeDrawers()
            return@OnNavigationItemSelectedListener true
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> mDrawerLayout.openDrawer(GravityCompat.START)
            R.id.idNotify -> {
                val intent = Intent(this,NotifyActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}