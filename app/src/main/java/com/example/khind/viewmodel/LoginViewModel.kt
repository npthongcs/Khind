package com.example.khind.viewmodel

import androidx.lifecycle.ViewModel
import com.example.khind.repository.LoginRepository

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()

    private var token: String = ""
    private var reToken: String = ""
    private var expiredAt: Long = 0
    private var email: String = ""
    private var avatar: String? = null

    fun getExpired(): Long {
        return expiredAt
    }

    fun setData(token: String, reToken: String, expired: Long, email: String, avatar: String?) {
        this.token = token
        this.reToken = reToken
        this.expiredAt = expired
        this.email = email
        this.avatar = avatar
    }

    fun getTokenLogin(): String {
        return token
    }

    fun getReTokenLogin(): String {
        return reToken
    }

    fun getEmailData(): String {
        return email
    }

    fun getAvatarLink(): String? {
        return avatar
    }

    fun getReTokenLiveDataObserver() = loginRepository.reTokenLiveDataObserver()

    fun getLoginLiveDataObserver() = loginRepository.loginLiveDataObserver()

    fun callAPILogin(email: String, password: String) = loginRepository.fetchLogin(email, password)

    fun callAPIRefreshToken(token: String, reToken: String) =
        loginRepository.fetchRefreshToken(token, reToken)
}