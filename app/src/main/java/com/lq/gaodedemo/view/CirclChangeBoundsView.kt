package com.lq.gaodedemo.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.View

/**
 * Created by liuqian on 2019/2/27 14:23
 * Describe:
 */

class CirclChangeBoundsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isStartDraw = false //是否需要开始绘制了

    private var greenPointColor = Color.parseColor("#00ff00")
    private var redPointColor = Color.parseColor("#ff0000")
    private var greenAlphaPointColor = Color.parseColor("#6600ff00")
    private var redAlphaPointColor = Color.parseColor("#66ff0000")
    private var grayPointColor = Color.parseColor("#BDBDBD")

    private var greenPaint: Paint = Paint()
    private var redPaint: Paint
    private var greenPaintAlpha: Paint
    private var redPaintAlpha: Paint
    private var grayPaint: Paint

    private var greenCircleRaidus: Float = 0.0f
    private var greenAlphaCircleRaidus: Float = 0.0f

    private var redStartY = 0.0f

    private var redCircleRaidus: Float = 0.0f
    private var redAlphaCircleRaidus: Float = 0.0f

    init {
        greenPaint.flags = Paint.ANTI_ALIAS_FLAG
        greenPaint.style = Paint.Style.FILL
        greenPaint.color = greenPointColor
        greenPaint.strokeWidth = 0.0f

        greenPaintAlpha = Paint(greenPaint)
        greenPaintAlpha.color = greenAlphaPointColor

        redPaint = Paint(greenPaint)
        redPaint.color = redPointColor

        redPaintAlpha = Paint(greenPaint)
        redPaintAlpha.color = redAlphaPointColor

        grayPaint = Paint(greenPaint)
        grayPaint.color = grayPointColor
    }

    private var drawCount = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isStartDraw) {
            //开始绘制第一个小绿点啊

            when (drawCount) {
                1 -> {
                    redStartY = (height / 4).toFloat()
                    canvas.drawCircle((width / 2).toFloat(), (height / 4).toFloat(), greenCircleRaidus, greenPaint)
                    canvas.drawCircle(
                        (width / 2).toFloat(),
                        (height / 4).toFloat(),
                        greenAlphaCircleRaidus,
                        greenPaintAlpha
                    )
                }

                2 -> {
                    canvas.drawCircle((width / 2).toFloat(), (height / 4).toFloat(), greenCircleRaidus, greenPaint)
                    canvas.drawCircle(
                        (width / 2).toFloat(),
                        (height / 4).toFloat(),
                        greenAlphaCircleRaidus,
                        greenPaintAlpha
                    )

                    canvas.drawCircle(
                        (width / 2).toFloat(), (height / 4 + height / 8 + height / 16).toFloat(), TypedValue
                            .applyDimension(COMPLEX_UNIT_PX, 5.0f, context.resources.displayMetrics), grayPaint
                    )
                    canvas.drawCircle(
                        (width / 2).toFloat(), (height / 2).toFloat(), TypedValue
                            .applyDimension(COMPLEX_UNIT_PX, 5.0f, context.resources.displayMetrics), grayPaint
                    )
                    canvas.drawCircle(
                        (width / 2).toFloat(), (height / 2 + height / 8 - height / 16).toFloat(), TypedValue
                            .applyDimension(COMPLEX_UNIT_PX, 5.0f, context.resources.displayMetrics), grayPaint
                    )

                    canvas.drawCircle((width / 2).toFloat(), redStartY, redCircleRaidus, redPaint)
                    canvas.drawCircle((width / 2).toFloat(), redStartY, redAlphaCircleRaidus, redPaintAlpha)
                }
            }
        }
    }

    fun startDraw() {
        isStartDraw = true
        drawCount = 1
        val greenAni = ObjectAnimator.ofFloat(
            this, "greenCircleRaidus", greenCircleRaidus, TypedValue
                .applyDimension(COMPLEX_UNIT_PX, 12.0f, context.resources.displayMetrics)
        )
        greenAni.duration = 600
        val greenAlphaAni = ObjectAnimator.ofFloat(
            this, "greenAlphaCircleRaidus", greenAlphaCircleRaidus, TypedValue
                .applyDimension(COMPLEX_UNIT_PX, 22.0f, context.resources.displayMetrics), 0.0f
        )
        greenAlphaAni.duration = 1500
        val set = AnimatorSet()
        set.playTogether(greenAni, greenAlphaAni)
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                startDrawRed()
            }
        })
        set.start()
    }

    private fun startDrawRed() {
        drawCount = 2
        val redYAni = ObjectAnimator.ofFloat(this, "redStartY", redStartY, (height * 3 / 4).toFloat())
        val redAni = ObjectAnimator.ofFloat(
            this, "redCircleRaidus", redCircleRaidus, TypedValue
                .applyDimension(COMPLEX_UNIT_PX, 12.0f, context.resources.displayMetrics)
        )
        val redAlphaAni = ObjectAnimator.ofFloat(
            this, "redAlphaCircleRaidus", redAlphaCircleRaidus, TypedValue
                .applyDimension(COMPLEX_UNIT_PX, 22.0f, context.resources.displayMetrics), 0.0f
        )
        val set = AnimatorSet()
        val set2 = AnimatorSet()
        set.duration = 800
        redAlphaAni.duration = 1500
        set.playTogether(redYAni, redAni)
        set2.playSequentially(set, redAlphaAni)
        set2.start()
    }


    fun getGreenCircleRaidus(): Float {
        return greenCircleRaidus
    }

    fun setGreenCircleRaidus(greenCircleRaidus: Float) {
        this.greenCircleRaidus = greenCircleRaidus
        postInvalidate()
    }

    fun getGreenAlphaCircleRaidus(): Float {
        return greenAlphaCircleRaidus
    }

    fun setGreenAlphaCircleRaidus(greenAlphaCircleRaidus: Float) {
        this.greenAlphaCircleRaidus = greenAlphaCircleRaidus
        postInvalidate()
    }


    fun getRedCircleRaidus(): Float {
        return redCircleRaidus
    }

    fun setRedCircleRaidus(redCircleRaidus: Float) {
        this.redCircleRaidus = redCircleRaidus
        postInvalidate()
    }

    fun getRedAlphaCircleRaidus(): Float {
        return redAlphaCircleRaidus
    }

    fun setRedAlphaCircleRaidus(redAlphaCircleRaidus: Float) {
        this.redAlphaCircleRaidus = redAlphaCircleRaidus
        postInvalidate()
    }


    fun getRedStartY(): Float {
        return redStartY
    }

    fun setRedStartY(redStartY: Float) {
        this.redStartY = redStartY
        postInvalidate()
    }

}