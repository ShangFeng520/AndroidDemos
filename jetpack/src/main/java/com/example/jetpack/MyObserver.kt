package com.example.jetpack

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.math.log

class MyObserver:LifecycleObserver {

    private val TAG = "---MyObserver"

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart(){
        Log.d(TAG, "activityStart")
    }

}