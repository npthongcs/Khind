package com.example.khind.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.khind.R
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.databinding.ActivityMainBinding
import com.example.khind.model.ResponseLogin
import com.example.khind.viewmodel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var viewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        makeObserver()
        getData()
    }

    private fun getData() {
        binding.btnLogin.setOnClickListener {
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()
            if (email!="" && password!="") {
                viewModel.callAPILogin(email,password)
            } else {
                Toast.makeText(this,"Email or password is empty",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeObserver() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.getLoginLiveDataObserver().observe(this,{
            if (it!=null){
                val intent = Intent(this,HomeActivity::class.java)
                intent.putExtra("token",it.data.token.token)
                intent.putExtra("reToken",it.data.token.refresh_token)
                startActivity(intent)
            } else {
                Toast.makeText(this,"Email or password is invalid",Toast.LENGTH_SHORT).show()
            }
        })
    }
}