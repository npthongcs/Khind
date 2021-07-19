package com.example.khind.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.khind.R
import com.example.khind.fragment.*
import com.example.khind.model.Sensor
import com.example.khind.model.Token
import com.example.khind.viewmodel.HomeViewModel
import com.example.khind.viewmodel.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlin.math.log
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity(){

    companion object{
        var loginViewModel = LoginViewModel()
    }

    private lateinit var mDrawerLayout: DrawerLayout
    private var homeViewModel = HomeViewModel()
    private lateinit var token: String
    private var expired by Delegates.notNull<Long>()
    private lateinit var reToken: String
    private var type: String = ""
    lateinit var txtAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        txtAddress = findViewById(R.id.nowSensor)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val user: Token = intent.getSerializableExtra("user") as Token
        token = user.token
        reToken = user.refresh_token
        expired = user.expired_at
        loginViewModel.setToken(token,reToken,expired)
        type = ""

        makeObserver()
        setupToolBar()
        setupMenuBar()
        setupBottomNavigation()
        setupOnClickChangeLocation()

        val fr = StatusFragment()
        loadFragment(fr)
        if (expired*1000>System.currentTimeMillis()) homeViewModel.callAPISensors(token)
        else {
            type = "GET_SENSOR"
            loginViewModel.callAPIRefreshToken(token,reToken)
        }
    }

    private fun setupOnClickChangeLocation() {
        val tvNowSensor = findViewById<TextView>(R.id.nowSensor)
        tvNowSensor.setOnClickListener {
            loadFragment(ChangeLocationFragment())
            if (expired*1000>System.currentTimeMillis()) homeViewModel.callAPISensors(token)
            else {
                type = "GET_SENSOR"
                loginViewModel.callAPIRefreshToken(token,reToken)
            }
        }
        val img = findViewById<ImageView>(R.id.imgChangeLo)
        img.setOnClickListener {
            loadFragment(StatusFragment())
        }
    }

    fun getViewModelLogin(): LoginViewModel {
        return loginViewModel
    }

    fun getViewModelHome(): HomeViewModel{
        return this.homeViewModel
    }

    fun setNameAddress(){
        txtAddress.text = homeViewModel.getNowSensorData()?.display_name ?: ""
    }

    private fun makeObserver() {
        loginViewModel.getReTokenLiveDataObserver().observe(this,{
            if (it!=null){
                token = it.data.token.token
                reToken = it.data.token.refresh_token
                expired = it.data.token.expired_at
                loginViewModel.setToken(token, reToken,expired)
                Log.d("refresh token home activity",token)
                when (type){
                    "GET_SENSOR" -> homeViewModel.callAPISensors(token)
                    "GET_DETAIL_SENSOR" -> homeViewModel.getNowSensorData()?.let { it1 ->
                        homeViewModel.callAPIDetailSensor(token, it1.id)
                    }
                }
                type = ""
            }
        })

        homeViewModel.getSensorsLiveDataObserver().observe(this,{
            if (it!=null){
                if (homeViewModel.getNowSensorData() == null) {
                    homeViewModel.setNowSensorData(it.data[0])
                }
            }
        })
    }


    private fun setupBottomNavigation() {
        val botNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        botNav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.status -> {
                    title = "Status"
                    loadFragment(StatusFragment())
                    if (homeViewModel.getNowSensorData()==null) {
                        if (expired*1000>System.currentTimeMillis()) homeViewModel.callAPISensors(token)
                        else {
                            type = "GET_SENSOR"
                            loginViewModel.callAPIRefreshToken(token,reToken)
                        }
                    }
                }
                R.id.mapview -> {
                    title = "Map"
                    val sensorID = homeViewModel.getNowSensorData()?.id
                    if (sensorID!=null) {
                        if (expired*1000>System.currentTimeMillis()) homeViewModel.callAPIDetailSensor(token, sensorID)
                        else {
                            type = "GET_DETAIL_SENSOR"
                            loginViewModel.callAPIRefreshToken(token,reToken)
                        }
                    }
                    loadFragment(MapFragment())
                }
                R.id.history -> {
                    title = "History"
                    loadFragment(HistoryFragment())
                }
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
        val titleToolbar = toolbar.findViewById<TextView>(R.id.title_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        titleToolbar.text = "Dashboard"

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
                R.id.my_profile -> loadFragment(ProfileFragment())
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