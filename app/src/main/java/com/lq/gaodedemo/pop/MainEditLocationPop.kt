package com.lq.gaodedemo.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.*
import android.widget.PopupWindow
import com.amap.api.services.core.PoiItem
import com.lq.gaodedemo.R

/**
 * Created by liuqian on 2019/2/26 17:09
 * Describe:
 */

class MainEditLocationPop @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) :
        PopupWindow(context, attrs, defStyleAttr) {

    private var popHeight = 0

    init {

        contentView = LayoutInflater.from(context).inflate(R.layout.layout_pop_main_edit, null)

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        popHeight  = contentView.measuredHeight

        width = context.resources.displayMetrics.widthPixels / 2
        height = WindowManager.LayoutParams.WRAP_CONTENT

        animationStyle = R.style.MyPopWindowMainLocation

        setBackgroundDrawable(ColorDrawable())

        isFocusable = true

        isOutsideTouchable = true

        isTouchable = true

        setTouchInterceptor { _, event ->
            /**
             * 判断是不是点击了外部
             */
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                true
            }
            //不是点击外部
            false
        }
    }

    private lateinit var listener: OnSearchResultClickListener

    fun setResultClickListener(listener: OnSearchResultClickListener) {
        this.listener = listener
    }

    interface OnSearchResultClickListener {
        fun onClickItem(position: Int, title: String, poiItem: PoiItem)
    }

    fun getPopHeight() : Int{
        return popHeight
    }
}