package com.lq.gaodedemo.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.lq.gaodedemo.R

/**
 * Created by liuqian on 2019/3/1 10:53
 * Describe:
 */

class LoadingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr) {


    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var bitmap: Bitmap? = null

    private var startAngle = 0.0f
    private var sweepAngle = 20.0f

    private var progressBarWidth = 0.0f
    private var margin = 0.0f

    private val bitmapWidth: Int
    private val bitmapHeight: Int

    init {

        visibility = View.GONE
        //绘制中间图标 全部绘制
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.feiji)
        bitmapWidth = bitmap!!.width
        bitmapHeight = bitmap!!.height

        progressBarWidth =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f, context.resources.displayMetrics)

        margin =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0f, context.resources.displayMetrics)

        progressPaint.color = Color.parseColor("#0098FF")
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.strokeWidth = progressBarWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rectSrc = Rect(0, 0, bitmapWidth, bitmapHeight)
        val rectDess = Rect(
                width / 2 - bitmapWidth / 2,
                height / 2 - bitmapHeight / 2,
                width / 2 + bitmapWidth / 2,
                height / 2 + bitmapHeight / 2
        )
        canvas.drawBitmap(bitmap, rectSrc, rectDess, null)  //左上角开始全部绘制

        val rectF = RectF()
        rectF.left = width / 2 - bitmapWidth / 2 - progressBarWidth - margin
        rectF.top = height / 2 - bitmapHeight / 2 - progressBarWidth - margin
        rectF.right = width / 2 + bitmapWidth / 2 + progressBarWidth + margin
        rectF.bottom = height / 2 + bitmapHeight / 2 + progressBarWidth + margin

        canvas.drawArc(rectF, startAngle, sweepAngle, false, progressPaint)
    }

    private val set = AnimatorSet()
    fun startAni() {
        visibility = View.VISIBLE
        val aniStart = ObjectAnimator.ofFloat(this, "startAngle", -90.0f, 270.0f)
        aniStart.repeatCount = -1
        val aniSweep = ObjectAnimator.ofFloat(this, "sweepAngle", 20.0f, 180.0f , 20.0f )
        aniSweep.repeatCount = -1
        set.playTogether(aniStart, aniSweep)
        set.duration = 500
        set.start()
    }

    fun closeAni() {
        visibility = View.GONE
        set.cancel()
    }

    private fun getStartAngle(): Float {
        return startAngle
    }

    private fun setStartAngle(startAngle: Float) {
        this.startAngle = startAngle
        postInvalidate()
    }

    private fun getSweepAngle(): Float {
        return sweepAngle
    }

    private fun setSweepAngle(sweepAngle: Float) {
        this.sweepAngle = sweepAngle
        postInvalidate()
    }

}