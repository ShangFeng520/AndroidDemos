package com.example.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.PlaceResponse
import com.google.gson.Gson

object PlaceDao {
    private const val savedKey = "place"
    private val sharedPreferences by lazy { SunnyWeatherApplication.context.getSharedPreferences("sunny_weather_place",Context.MODE_PRIVATE) }

    fun savePlace(place:PlaceResponse.Place){
        sharedPreferences.edit {
            putString(savedKey,Gson().toJson(place))
        }
    }

    fun getSavedPlace():PlaceResponse.Place?{
        val placeJson = sharedPreferences.getString(savedKey,null)
        return Gson().fromJson(placeJson,PlaceResponse.Place::class.java)
    }
}