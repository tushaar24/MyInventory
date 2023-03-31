package com.example.myinventory

import com.example.myinventory.utils.NetworkStatusChecker
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myinventory.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var networkStatusChecker: NetworkStatusChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        networkStatusChecker = NetworkStatusChecker(this, binding.root)
        networkStatusChecker.register()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        networkStatusChecker.unregister()
    }
}