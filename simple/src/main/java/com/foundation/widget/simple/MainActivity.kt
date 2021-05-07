package com.foundation.widget.simple

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.foundation.widget.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            binding.lv.showLoading()
            binding.lc.loadingView.showLoading()
        }
        binding.btnStop.setOnClickListener {
            binding.lv.stopLoading()
            binding.lc.loadingView.stopLoading()
        }
        binding.btnShowFail.setOnClickListener {
            binding.lv.showLoadingFail(true)
            binding.lc.loadingView.showLoadingFail(true)
        }
        binding.lc.loadingView.failViewClickListener = {
            Toast.makeText(MainActivity@ this, "lc fail click", Toast.LENGTH_LONG).show()
        }
        binding.lv.failViewClickListener = {
            Toast.makeText(MainActivity@ this, "lv fail click", Toast.LENGTH_LONG).show()
        }
        binding.btnShow.setOnClickListener {
            binding.lv.showLoadingState()
            binding.lc.loadingView.showLoadingState()
        }
    }

}