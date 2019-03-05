package com.lq.gaodedemo.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.lq.gaodedemo.NearbyBean
import com.lq.gaodedemo.R
import com.lq.gaodedemo.adapter.NearbyAdapter
import kotlinx.android.synthetic.main.activity_nearby.*

/**
 * Created by liuqian on 2019/3/4 14:32
 * Describe:
 */
class NearbyActivity : AppCompatActivity() {

    private lateinit var adapter: NearbyAdapter

    private val listData = ArrayList<NearbyBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby)

        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_chevron_left_white_28dp)
        toolbar.title = "Hello Man ..."
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"))
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        init()
        setData()
    }

    private fun init() {
        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView.layoutManager = gridLayoutManager
        adapter = NearbyAdapter(listData)
        recyclerView.adapter = adapter
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (listData[position].type) {
                    1, 2 -> gridLayoutManager.spanCount //title
                    else -> 1
                }
            }
        }
    }

    private fun setData() {
        val strings = arrayOf("美食", "商场", "酒店", "景点", "地铁站", "银行", "便民服务", "超市", "厕所", "更多")
        for (item in 0..9) {
            val bean = NearbyBean()
            bean.title = strings[item]
            bean.type = 0
            listData.add(bean)
        }
        val titleBean = NearbyBean()
        titleBean.type = 1
        titleBean.title = "宇宙中心地球村"
        listData.add(titleBean)

        val GridBean = NearbyBean()
        GridBean.type = 2
        GridBean.title = ""
        listData.add(GridBean)

        adapter.setNewData(listData)
    }
}