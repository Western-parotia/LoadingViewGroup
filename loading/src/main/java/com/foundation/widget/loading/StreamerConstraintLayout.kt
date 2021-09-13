package com.foundation.widget.loading

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * 流光viewGroup，继承ConstraintLayout
 * 在子view 绘制完成后 合成流光动画
 * 使用方法：[start] [stop]
 * @param context
 * @param attributeSet
 */
class StreamerConstraintLayout(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    constructor(context: Context) : this(context, null)

    private val DEFAULT_STREAMER_WIDTH = 30F.dp
    private val DEFAULT_COLOR = Color.parseColor("#F2F4F7")
    private val DEFAULT_DURATION = 5000L
    private val DEFAULT_SKIP_COUNT = 1

    private val path = Path()
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
    var streamerWidth: Float
    var streamerColor: Int
    var animDuration: Long
    var skipCount = DEFAULT_SKIP_COUNT

    private val paintStreamer = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0F

    private var animPlayCount = 0
    private val floatAnim by lazy {
        ObjectAnimator.ofFloat(0F, 1F).apply {
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    animPlayCount += 1
                }

            })
            addUpdateListener {
                if (animPlayCount == 0 || animPlayCount % skipCount == 0) {
                    progress = it.animatedValue as Float
                    postInvalidate()
                }
            }
        }
    }

    init {
        if (null != attributeSet) {
            val typeArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.StreamerConstraintLayout)
            streamerWidth = typeArray.getDimensionPixelOffset(
                R.styleable.StreamerConstraintLayout_sc_width,
                DEFAULT_STREAMER_WIDTH.toInt()
            ).toFloat()

            streamerColor =
                typeArray.getColor(R.styleable.StreamerConstraintLayout_sc_color, DEFAULT_COLOR)
            animDuration = typeArray.getInt(
                R.styleable.StreamerConstraintLayout_sc_duration,
                DEFAULT_DURATION.toInt()
            ).toLong()
            skipCount = typeArray.getInt(
                R.styleable.StreamerConstraintLayout_sc_skip_count,
                DEFAULT_SKIP_COUNT
            )

            typeArray.recycle()
        } else {
            streamerWidth = DEFAULT_STREAMER_WIDTH
            streamerColor = DEFAULT_COLOR
            animDuration = DEFAULT_DURATION
        }
    }

    private fun correctionPath() {
        path.reset()
        val offsetX = 14.dp.toFloat()
        val startX = -streamerWidth * 2 - offsetX
        val startY = 0F
        val endY = height.toFloat()
        path.moveTo(startX, startY)
        path.lineTo(-streamerWidth - offsetX, startY)
        path.lineTo(0F, endY)
        path.lineTo(-streamerWidth, endY)
        path.close()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val count = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        val translateX = progress * (width + streamerWidth)
        val translateY = 0F
        super.dispatchDraw(canvas)//内容
        paintStreamer.xfermode = xfermode
        paintStreamer.color = streamerColor
        canvas.translate(translateX, -translateY)
        canvas.drawPath(path, paintStreamer)
        paintStreamer.xfermode = null
        canvas.translate(-translateX, translateY)
        canvas.restoreToCount(count)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        floatAnim.cancel()
    }

    fun start() {
        correctionPath()
        floatAnim.duration = animDuration
        floatAnim.start()
    }

    fun stop() {
        progress = 1.0F
        invalidate()
        floatAnim.cancel()
        animPlayCount = 0
    }
}