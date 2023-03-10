package com.example.sunnyweather.ui.place

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.MainActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.FragmentPlaceBinding
import com.example.sunnyweather.ui.base.BaseFragment
import com.example.sunnyweather.ui.place.PlaceAdapter
import com.example.sunnyweather.ui.place.PlaceViewModel
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceFragment : BaseFragment<FragmentPlaceBinding>() {
    internal val placeViewModel by lazy { ViewModelProvider(this)[PlaceViewModel::class.java] }
    private lateinit var adapter: PlaceAdapter

    override fun onCreateViewBinding(inflater: LayoutInflater, parent: ViewGroup?): FragmentPlaceBinding {
        return FragmentPlaceBinding.inflate(inflater,parent,false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity){
            placeViewModel.getSavedPlace()?.let { place->
                val intent = Intent(context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                startActivity(intent)
                activity?.finish()
                return
            }
        }

        val layoutManager = LinearLayoutManager(activity)
        adapter = PlaceAdapter(this,placeViewModel.placeList)
        with(binding){
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter=adapter
            searchPlaceEdit.addTextChangedListener {
                val content = it.toString()
                if (content.isNotEmpty()){
                    placeViewModel.searchPlaces(content)
                }else{
                    recyclerView.visibility = View.GONE
                    bgImageView.visibility = View.VISIBLE
                    placeViewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
            placeViewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result->
                val places = result.getOrNull()
                if (places!=null){
                    recyclerView.visibility = View.VISIBLE
                    bgImageView.visibility = View.GONE
                    placeViewModel.placeList.clear()
                    placeViewModel.placeList.addAll(places)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                    result.exceptionOrNull()?.printStackTrace()
                }
            })
        }
    }

}