package com.foundation.widget.simple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.foundation.widget.loading.LoadingViewGroup
import com.foundation.widget.simple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
        binding.lv.showLoading()
        }
        binding.btnStop.setOnClickListener {
            binding.lv.stopLoading()
        }
        binding.btnShowFail.setOnClickListener {
            binding.lv.showLoadingFail(true)
        }
        binding.lv.failViewClickListener={
            Toast.makeText(MainActivity@this,"fail click",Toast.LENGTH_LONG).show()
        }

    }

}