package com.example.khind.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.khind.model.Message
import com.example.khind.model.ResponseMessage
import com.example.khind.repository.NotifyRepository

class NotifyViewModel : ViewModel() {
    private val notifyRepository = NotifyRepository()
    var listMessage = ArrayList<Message>()

    fun getListMessageData(): ArrayList<Message>{
        return listMessage
    }
    fun setListMessageData(data: ArrayList<Message>){
        listMessage.addAll(data)
    }

    fun callApiGetMessages(token: String, page: Int){
        notifyRepository.fetchMessages(token, page)
    }

    fun getMessagesLiveDataObserver(): MutableLiveData<ResponseMessage>{
        return notifyRepository.messagesLiveDataObserver()
    }
}