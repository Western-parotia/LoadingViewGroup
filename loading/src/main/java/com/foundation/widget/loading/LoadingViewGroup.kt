package com.foundation.widget.loading

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView

/**
 *@Desc:
 *-
 *-
 *create by zhusw on 5/6/21 17:08
 */

class LoadingViewGroup(context: Context,attributeSet: AttributeSet?):ViewGroup(context,attributeSet) {
    constructor(context: Context):this(context,null)

   private val undergroundImg = AppCompatImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = LayoutParams(MATCH_PART,MATCH_PART)
        addView(this)
    }
    private val loadingTextView = TextView(context).apply {
        text = "loading"
        gravity = Gravity.CENTER
        layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        setBackgroundColor(Color.BLUE)
        addView(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        undergroundImg.autoMeasure(this)
        loadingTextView.autoMeasure(this)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        undergroundImg.autoLayoutToCenter(this)
        loadingTextView.autoLayoutToCenter(this)
    }

    fun setUndergroundImg(id:Int){
        undergroundImg.setBackgroundResource(id)
    }
    fun setLoadingTxt(text:String){
        loadingTextView.text = text
    }
}
