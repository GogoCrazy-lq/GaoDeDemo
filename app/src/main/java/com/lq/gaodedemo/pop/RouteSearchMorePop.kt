package com.lq.gaodedemo.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.*
import android.view.View.OVER_SCROLL_NEVER
import android.widget.PopupWindow
import android.widget.RelativeLayout
import com.amap.api.services.core.PoiItem
import com.lq.gaodedemo.R
import com.lq.gaodedemo.adapter.RouteSearchMoreAdapter

/**
 * Created by liuqian on 2019/2/26 17:09
 * Describe:
 */

class RouteSearchMorePop @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) :
        PopupWindow(context, attrs, defStyleAttr) {

    companion object {
        val moreType = arrayOf(
            R.drawable.ic_send_black_24dp,
            R.drawable.ic_directions_car_black_24dp,
            R.drawable.ic_directions_bike_black_24dp,
            R.drawable.ic_directions_walk_black_24dp,
            R.drawable.ic_airport_shuttle_black_24dp,
            R.drawable.ic_local_shipping_black_24dp,
            R.drawable.ic_local_taxi_black_24dp,
            R.drawable.ic_directions_bus_black_24dp,
            R.drawable.ic_directions_transit_black_24dp,
            R.drawable.ic_flight_black_24dp,
            R.drawable.ic_motorcycle_black_24dp
        )

        val listStr = arrayOf("易通", "驾车", "骑行", "步行", "客车", "货车", "打车", "公交", "火车", "飞机", "摩托")
    }

    private var recyclerView: RecyclerView

    private var routeSearchMoreAdapter: RouteSearchMoreAdapter

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_route_search_more_list, null)

        recyclerView = contentView!!.findViewById(R.id.recyclerView)
        recyclerView.layoutParams = RelativeLayout.LayoutParams(
                context.resources.displayMetrics.widthPixels / 5 * 3,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        routeSearchMoreAdapter =
            RouteSearchMoreAdapter(R.layout.item_route_search_more)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.overScrollMode = OVER_SCROLL_NEVER
        recyclerView.adapter = routeSearchMoreAdapter

        width = context.resources.displayMetrics.widthPixels / 5 * 3
        height = WindowManager.LayoutParams.WRAP_CONTENT

        animationStyle = R.style.MyPopWindowMore

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

        setPoiData()
    }

    fun setPoiData() {

        routeSearchMoreAdapter.setNewData(listStr.asList())
    }

    private lateinit var listener: OnSearchResultClickListener

    fun setResultClickListener(listener: OnSearchResultClickListener) {
        this.listener = listener
    }

    interface OnSearchResultClickListener {
        fun onClickItem(position: Int, title: String, poiItem: PoiItem)
    }

}