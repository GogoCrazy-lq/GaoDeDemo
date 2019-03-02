package com.lq.gaodedemo.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.lq.gaodedemo.R

/**
 * Created by liuqian on 2019/3/1 17:30
 * Describe:
 */
class LoadingView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var bitmap: Bitmap? = null
    private val bitmapWidth: Int
    private val bitmapHeight: Int
    private var circleRaduis = 0.0f
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)




    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.feiji_white)
        bitmapWidth = bitmap!!.width
        bitmapHeight = bitmap!!.height

        circleRaduis = bitmapWidth / 2 + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15.0f, context.resources.displayMetrics)

        circlePaint.color = Color.parseColor("#0098FF")
        circlePaint.style = Paint.Style.FILL
        circlePaint.strokeWidth = 0.0f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rectSrc = Rect(0, 0, bitmapWidth, bitmapHeight)
        val rectDess = Rect(
                width / 2 - bitmapWidth / 2,
                height / 2 - bitmapHeight / 2,
                width / 2 + bitmapWidth / 2,
                height / 2 + bitmapHeight / 2
        )
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), circleRaduis, circlePaint)
        canvas.drawBitmap(bitmap, rectSrc, rectDess, null)  //左上角开始全部绘制
    }

}