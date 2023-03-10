package com.example.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MainViewModel(countReserved:Int):ViewModel() {

    private val userLiveData = MutableLiveData<User>()

    val userName:LiveData<String> = Transformations.map(userLiveData){
        "${it.firstName} ${it.lastName}"
    }

    val counter:LiveData<Int>
        get() = _counter

    val _counter = MutableLiveData<Int>()

    init {
        _counter.value = countReserved
    }

    fun plusOne(){
        val count = _counter.value?:0
        _counter.value=count+1
    }

    fun clear(){
        _counter.value = 0
    }

    private val userIdLiveData = MutableLiveData<String>()

    val user:LiveData<User> = Transformations.switchMap(userIdLiveData){
        getUserFromNet(it)
    }

    fun getUser(userId: String){
        userIdLiveData.value = userId
    }

    private fun getUserFromNet(userId:String):LiveData<User>{
        val liveData = MutableLiveData<User>()
        liveData.value = User(userId,userId,0)
        return liveData
    }
}