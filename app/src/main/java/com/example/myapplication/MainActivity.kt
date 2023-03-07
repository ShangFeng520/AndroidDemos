package com.example.myapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import kotlin.concurrent.thread
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    private val TAG = "---MainActivity"
    private lateinit var binding:ActivityMainBinding

    val fruits = mutableListOf(Fruit("Apple", R.drawable.apple), Fruit("Banana", R.drawable.banana), Fruit("Orange", R.drawable.orange), Fruit("Watermelon", R.drawable.watermelon), Fruit("Pear", R.drawable.pear), Fruit("Grape", R.drawable.grape), Fruit("Pineapple", R.drawable.pineapple), Fruit("Strawberry", R.drawable.strawberry), Fruit("Cherry", R.drawable.cherry), Fruit("Mango", R.drawable.mango))
    val fruitList = ArrayList<Fruit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        binding.navView.setCheckedItem(R.id.navCall)
        binding.navView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawers()
            true
        }
        binding.fab.setOnClickListener{
            Snackbar.make(it,"Data deleted", Snackbar.LENGTH_SHORT)
                .setAction("Undo"){
                    Toast.makeText(this,"Data restored",Toast.LENGTH_SHORT).show()
                }.show()
        }

        initFruits()
        val gridLayoutManager = GridLayoutManager(this, 2)
        val fruitAdapter = FruitAdapter(this, fruitList)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = fruitAdapter

        binding.swipeRefresh.setOnRefreshListener {
            refreshFruits()
        }
    }

    private fun initFruits(){
        fruitList.clear()
        repeat(50){
            val index = (0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }

    private fun refreshFruits(){
        thread {
            Thread.sleep(2000)
            runOnUiThread{
                initFruits()
                binding.recyclerView.adapter!!.notifyDataSetChanged()
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.backup -> Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show()
            R.id.delete -> Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}

fun main() {

}

fun View.showSnackBar(text:String,actionText:String? = null,duration:Int = Snackbar.LENGTH_SHORT,block:(()->Unit)? = null) {
    val snackBar = Snackbar.make(this,text,duration)
    if (actionText!=null && block!=null){
        snackBar.setAction(actionText){
            block()
        }
    }
    snackBar.show()
}