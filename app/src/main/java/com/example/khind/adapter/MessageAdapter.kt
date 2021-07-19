package com.example.khind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khind.databinding.MessageItemBinding
import com.example.khind.listener.MessageOnClickListener
import com.example.khind.model.Message
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(private val mMessages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var messageOnClickListener: MessageOnClickListener? = null

    fun setOnCallBackListener(messageOnClickListener: MessageOnClickListener){
        this.messageOnClickListener = messageOnClickListener
    }

    inner class ViewHolder(private val binding: MessageItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: Message){
            val date = Date(data.created_at)
            val format = SimpleDateFormat("dd MMM, hh:mma")
            binding.date = format.format(date)
            binding.title = data.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(MessageItemBinding.inflate(inflater,parent,false)).apply {
            itemView.setOnClickListener {
                this@MessageAdapter.messageOnClickListener?.onMessageClick(mMessages[bindingAdapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mMessages[position])
    }

    override fun getItemCount(): Int {
        return mMessages.size
    }
}