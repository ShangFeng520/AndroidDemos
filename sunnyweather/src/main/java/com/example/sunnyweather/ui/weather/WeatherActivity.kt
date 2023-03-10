package com.example.sunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    internal val weatherViewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }
    lateinit var binding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(weatherViewModel){
            locationLng = intent.getStringExtra("location_lng") ?: ""
            locationLat = intent.getStringExtra("location_lat") ?: ""
            placeName = intent.getStringExtra("place_name") ?: ""

            weatherLiveData.observe(this@WeatherActivity){result->
                val weather = result.getOrNull()
                if (weather!=null){
                    showWeatherInfo(weather)
                }else{
                    Toast.makeText(this@WeatherActivity, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                    result.exceptionOrNull()?.printStackTrace()
                }
                binding.swipeRefresh.isRefreshing = false
            }
        }
        refreshWeather()
        binding.swipeRefresh.setColorSchemeResources(R.color.purple_700)
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        binding.nowLayout.navBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(binding.drawerLayout.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)

            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    internal fun refreshWeather(){
        weatherViewModel.refreshWeather(weatherViewModel.locationLng,weatherViewModel.locationLat)
        binding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather){
        val realtime = weather.realtime
        val daily = weather.daily

        // 填充now.xml布局中数据
        with(binding.nowLayout){
            val sky = getSky(realtime.skycon)
            placeName.text = weatherViewModel.placeName
            val currentTempText = "${realtime.temperature.toInt()} ℃"
            currentTemp.text = currentTempText
            currentSky.text = sky.info
            val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
            currentAQI.text = currentPM25Text
            nowLayout.setBackgroundResource(sky.bg)
        }
        // 填充forecast.xml布局中的数据 这边没有用recyclerView
        with(binding.forecastLayout){
            forecastLayout.removeAllViews()
            val days = daily.skycon.size
            for (i in 0 until days) {
                val skycon = daily.skycon[i]
                val temperature = daily.temperature[i]
                val view = LayoutInflater.from(this@WeatherActivity).inflate(R.layout.forecast_item, forecastLayout, false)
                val dateInfo = view.findViewById(R.id.dateInfo) as TextView
                val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
                val skyInfo = view.findViewById(R.id.skyInfo) as TextView
                val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateInfo.text = simpleDateFormat.format(skycon.date)
                val sky = getSky(skycon.value)
                skyIcon.setImageResource(sky.icon)
                skyInfo.text = sky.info
                val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                temperatureInfo.text = tempText
                forecastLayout.addView(view)
            }
        }
        // 填充life_index.xml布局中的数据
        with(binding.lifeIndexLayout){
            val lifeIndex = daily.lifeIndex
            coldRiskText.text = lifeIndex.coldRisk[0].desc
            dressingText.text = lifeIndex.dressing[0].desc
            ultravioletText.text = lifeIndex.ultraviolet[0].desc
            carWashingText.text = lifeIndex.carWashing[0].desc
        }
        binding.weatherLayout.visibility = View.VISIBLE
    }
}