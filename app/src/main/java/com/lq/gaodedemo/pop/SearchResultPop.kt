package com.lq.gaodedemo.pop

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.PopupWindow
import com.amap.api.services.core.PoiItem
import com.lq.gaodedemo.R
import com.lq.gaodedemo.adapter.SearchResultAdapter

/**
 * Created by liuqian on 2019/2/26 17:09
 * Describe:
 */

class SearchResultPop @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    PopupWindow(context, attrs, defStyleAttr) {

    private var recyclerView: RecyclerView

    private var searchResultAdapter: SearchResultAdapter

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_search, null)

        recyclerView = contentView!!.findViewById(R.id.recyclerView)
        searchResultAdapter = SearchResultAdapter(R.layout.item_search_result_text)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(context.resources.getDrawable(R.drawable.shape_divier))
        recyclerView.addItemDecoration(dividerItemDecoration)
        searchResultAdapter.setOnItemClickListener { adapter, view, position ->
            if (listener != null) {
                listener.onClickItem(
                    position,
                    (adapter.data[position] as PoiItem).title,
                    adapter.data[position] as PoiItem
                )
            }
        }
        recyclerView.adapter = searchResultAdapter

        width = WindowManager.LayoutParams.MATCH_PARENT
        height = context.resources.displayMetrics.heightPixels / 2

        animationStyle = R.style.MyPopWindow

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

    fun setPoiData(listItem: List<PoiItem>) {
        searchResultAdapter.setNewData(listItem)
    }

    private lateinit var listener: OnSearchResultClickListener

    fun setResultClickListener(listener: OnSearchResultClickListener) {
        this.listener = listener
    }

    interface OnSearchResultClickListener {
        fun onClickItem(position: Int, title: String, poiItem: PoiItem)
    }

}