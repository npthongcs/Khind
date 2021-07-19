package com.example.khind.viewmodel

import androidx.lifecycle.ViewModel
import com.example.khind.repository.LoginRepository

class LoginViewModel: ViewModel() {
    private val loginRepository = LoginRepository()

    var token: String = ""
    var reToken: String = ""
    private var expiredAt: Long? = 0

    fun getExpired(): Long? {
        return expiredAt
    }

    fun setToken(token: String, reToken: String, expired: Long){
        this.token = token
        this.reToken = reToken
        expiredAt = expired
    }

    fun getTokenLogin():String {
        return token
    }

    fun getReTokenLogin():String {
        return reToken
    }

    fun getReTokenLiveDataObserver() = loginRepository.reTokenLiveDataObserver()

    fun getLoginLiveDataObserver() = loginRepository.loginLiveDataObserver()

    fun callAPILogin(email: String, password: String) = loginRepository.fetchLogin(email,password)

    fun callAPIRefreshToken(token: String, reToken: String) = loginRepository.fetchRefreshToken(token, reToken)
}