package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.MainActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.PlaceResponse
import com.example.sunnyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val fragment: PlaceFragment,private val placeList: List<PlaceResponse.Place>):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName: TextView = view.findViewById<TextView>(R.id.placeName)
        val placeAddress: TextView = view.findViewById<TextView>(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = fragment.layoutInflater.inflate(R.layout.place_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener{
            val pos = holder.adapterPosition
            val place = placeList[pos]
            val activity = fragment.activity
            if (activity is WeatherActivity){
                activity.binding.drawerLayout.closeDrawers()
                activity.weatherViewModel.locationLng = place.location.lng
                activity.weatherViewModel.locationLat = place.location.lat
                activity.weatherViewModel.placeName = place.name
                activity.refreshWeather()
            }else{
                val intent = Intent(fragment.activity,WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
            fragment.placeViewModel.savePlace(place)
        }
        return holder
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }
}