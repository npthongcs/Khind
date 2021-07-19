package com.example.khind.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khind.R
import com.example.khind.activity.HomeActivity
import com.example.khind.activity.NotifyActivity
import com.example.khind.adapter.MessageAdapter
import com.example.khind.listener.MessageOnClickListener
import com.example.khind.model.Message
import com.example.khind.viewmodel.NotifyViewModel
import kotlin.properties.Delegates


class MessageFragment : Fragment(), MessageOnClickListener {

    private val loginViewModel = HomeActivity().getViewModelLogin()
    private lateinit var notifyViewModel: NotifyViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var rvMessage: RecyclerView
    private var expired by Delegates.notNull<Long>()
    var pageCount = 1
    var isLoad = false
    var isMessage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        notifyViewModel = ViewModelProvider(this).get(NotifyViewModel::class.java)
        messageAdapter = MessageAdapter(notifyViewModel.getListMessageData())
        rvMessage = view.findViewById<RecyclerView>(R.id.rvMessage)
        expired = loginViewModel.getExpired()!!

        makeObserver()

        if (expired*1000>System.currentTimeMillis()) notifyViewModel.callApiGetMessages(loginViewModel.token,pageCount)
        else {
            isMessage = true
            loginViewModel.callAPIRefreshToken(loginViewModel.getTokenLogin(),loginViewModel.getReTokenLogin())
        }

        rvMessage.apply {
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
            adapter = messageAdapter
        }

        initScrollListener()
        messageAdapter.setOnCallBackListener(this)
        return view
    }

    private fun initScrollListener() {
        rvMessage.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = rvMessage.layoutManager as LinearLayoutManager
                if (!isLoad){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition()==messageAdapter.itemCount-1){
                        isLoad = true
                        pageCount++
                        if (expired*1000>System.currentTimeMillis()) notifyViewModel.callApiGetMessages(loginViewModel.token,pageCount)
                        else {
                            isMessage = true
                            loginViewModel.callAPIRefreshToken(loginViewModel.getTokenLogin(),loginViewModel.getReTokenLogin())
                        }
                    }
                }
            }
        })
    }

    private fun makeObserver() {
        loginViewModel.getReTokenLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null && isMessage){
                notifyViewModel.callApiGetMessages(it.data.token.token,pageCount)
                isMessage = false
            }
        })

        notifyViewModel.getMessagesLiveDataObserver().observe(viewLifecycleOwner,{
            if (it!=null){
                Log.d("it fr",it.toString())
                notifyViewModel.setListMessageData(it.data)
                if (pageCount==1) messageAdapter.notifyDataSetChanged()
                else {
                    val currentSize = messageAdapter.itemCount
                    messageAdapter.notifyItemRangeInserted(currentSize,it.data.size)
                }
                isLoad = false
            }
        })
    }

    override fun onMessageClick(data: Message) {
        Log.d("message clicked",data.title)
    }

}