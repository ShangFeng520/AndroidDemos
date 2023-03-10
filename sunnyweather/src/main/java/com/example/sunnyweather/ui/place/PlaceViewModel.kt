package com.example.sunnyweather.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.PlaceResponse

class PlaceViewModel:ViewModel() {

    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<PlaceResponse.Place>()

    val placeLiveData:LiveData<Result<List<PlaceResponse.Place>>> = Transformations.switchMap(searchLiveData){
        Repository.searchPlaces(it)
    }

    fun searchPlaces(query:String){
        searchLiveData.value = query
    }

    fun savePlace(place:PlaceResponse.Place) = Repository.savePlace(place)

    fun getSavedPlace(): PlaceResponse.Place? = Repository.getSavedPlace()
}