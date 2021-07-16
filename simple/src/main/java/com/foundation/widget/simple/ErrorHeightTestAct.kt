package com.foundation.widget.simple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foundation.widget.simple.databinding.ActErrorHeightBinding

/**
 * create by zhusw on 7/15/21 10:58
 */
class ErrorHeightTestAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vb = ActErrorHeightBinding.inflate(layoutInflater)
        setContentView(vb.root)


    }

}