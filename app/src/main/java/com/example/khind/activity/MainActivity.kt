package com.example.khind.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.khind.R
import com.example.khind.RetroInstance
import com.example.khind.`interface`.ApiService
import com.example.khind.databinding.ActivityMainBinding
import com.example.khind.model.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        getData()
    }

    private fun getData() {
        binding.btnLogin.setOnClickListener {
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()
            if (email!="" && password!="") {
                makeCallApi(email,password)
            } else {
                Toast.makeText(this,"Email or password is empty",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun makeCallApi(email: String, password: String) {
        val retroInstance = RetroInstance.getRetroInstance().create(ApiService::class.java)
        val call = retroInstance.login(email,password)
        call.enqueue(object : Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.code()==200){
                    Log.d("response login",response.body().toString())
                } else {
                    Toast.makeText(this@MainActivity,"Email or password is invalid",Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.d("call api login","failed")
            }
        })
    }
}