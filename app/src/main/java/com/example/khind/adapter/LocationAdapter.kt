package com.example.khind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.khind.databinding.LocationGreenItemBinding
import com.example.khind.databinding.LocationOrangeItemBinding
import com.example.khind.databinding.LocationRedItemBinding
import com.example.khind.listener.LocationOnClickListener
import com.example.khind.model.Sensor

class LocationAdapter(var mSensor: ArrayList<Sensor>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var locationOnClickListener: LocationOnClickListener? = null

    fun setOnCallBackListener(locationOnClickListener: LocationOnClickListener){
        this.locationOnClickListener = locationOnClickListener
    }

    inner class RedViewHolder(private val binding: LocationRedItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: String){
            binding.nameLocation = data
        }
    }

    inner class OrangeViewHolder(private val binding: LocationOrangeItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: String){
            binding.nameLocation = data
        }
    }

    inner class GreenViewHolder(private val binding: LocationGreenItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: String){
            binding.nameLocation = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            0 -> GreenViewHolder(LocationGreenItemBinding.inflate(inflater,parent,false)).apply {
                itemView.setOnClickListener {
                    this@LocationAdapter.locationOnClickListener?.onItemClick(mSensor[bindingAdapterPosition])
                }
            }
            1 -> OrangeViewHolder(LocationOrangeItemBinding.inflate(inflater,parent,false)).apply {
                itemView.setOnClickListener {
                    this@LocationAdapter.locationOnClickListener?.onItemClick(mSensor[bindingAdapterPosition])
                }
            }
            else -> RedViewHolder(LocationRedItemBinding.inflate(inflater,parent,false)).apply {
                itemView.setOnClickListener {
                    this@LocationAdapter.locationOnClickListener?.onItemClick(mSensor[bindingAdapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)){
            0 -> (holder as GreenViewHolder).bind(mSensor[position].display_name)
            1 -> (holder as OrangeViewHolder).bind(mSensor[position].display_name)
            2 -> (holder as RedViewHolder).bind(mSensor[position].display_name)
        }
    }

    override fun getItemCount(): Int {
        return mSensor.size
    }

    override fun getItemViewType(position: Int): Int {
        val sensor = mSensor[position]
        return when (sensor.alarm){
            "clear" -> 0
            "warning" ->1
            "alert" -> 2
            else -> -1
        }
    }
}