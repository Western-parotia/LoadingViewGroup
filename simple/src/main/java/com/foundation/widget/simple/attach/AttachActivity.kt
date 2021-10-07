package com.foundation.widget.simple.attach

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.foundation.widget.loading.PageLoadingView
import com.foundation.widget.simple.R

/**
 * create by zhusw on 7/16/21 18:02
 */
class AttachActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_attach)
        val vp = findViewById<ViewPager2>(R.id.vpList)
        vp.adapter = ViewPager2FragmentAdapter(this).apply {
            list = arrayListOf(Tf(), Fragment(), Fragment(), Fragment(), Tf())
        }

        findViewById<View>(R.id.bt0).setOnClickListener {
            vp.currentItem = 0
        }

        findViewById<View>(R.id.bt4).setOnClickListener {
            vp.currentItem = 4
        }
    }

    class Tf : Fragment() {
        private val handler = Handler()
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            return PageLoadingView(requireActivity()).apply {
                setLoadingAdapter(ArchLoadingAdapter(requireActivity()) {
                    showLoading(this)
                })
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            showLoading(view as PageLoadingView)
        }

        private fun showLoading(plv: PageLoadingView) {
            plv.showLoading(true)
            handler.postDelayed({ plv.showEmptyView() }, 5000)
        }

        override fun onDestroyView() {
            handler.removeCallbacksAndMessages(null)
            super.onDestroyView()
        }
    }
}