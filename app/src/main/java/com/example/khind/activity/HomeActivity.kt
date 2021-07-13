package com.example.khind.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.khind.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {

    lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupToolBar()
        setupMenuBar()
        setupBottomNavigation()

    }

    private fun setupBottomNavigation() {
        val botNav: BottomNavigationView = findViewById(R.id.bottom_nav)
        botNav.setOnNavigationItemSelectedListener {
            when (it.itemId){
                R.id.status -> Toast.makeText(this,"status",Toast.LENGTH_SHORT).show()
                R.id.mapview -> Toast.makeText(this,"map view",Toast.LENGTH_SHORT).show()
                R.id.history -> Toast.makeText(this,"history",Toast.LENGTH_SHORT).show()
                else -> false
            }
            return@setOnNavigationItemSelectedListener true
        }

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