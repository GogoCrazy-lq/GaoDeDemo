package com.lq.gaodedemo.activity

import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.gyf.immersionbar.ImmersionBar
import com.lq.gaodedemo.FuckApplication
import com.lq.gaodedemo.R
import com.lq.gaodedemo.adapter.SearchAdapter
import com.lq.gaodedemo.bean.SearchMultiItemBean
import kotlinx.android.synthetic.main.activity_search.*

/**
 * POI搜索
 * 实现 输入内容自动提示
 * 搜索按钮动画
 */
class SearchActivity : AppCompatActivity(), Inputtips.InputtipsListener {

    private val items = ArrayList<SearchMultiItemBean<*>>()
    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ImmersionBar
            .with(this)
            .statusBarDarkFont(true)
            .statusBarColor(android.R.color.white)
            .navigationBarColor(android.R.color.black)
            .init()
        initSet()
    }

    private var tvSearchStatus = false

    private fun initSet() {
        recyclerView
            .apply {
                layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(this@SearchActivity, RecyclerView.VERTICAL)
                    .apply {
                        setDrawable(ContextCompat.getDrawable(this@SearchActivity, R.drawable.shape_divider)!!)
                    })
            }.adapter = SearchAdapter(items)
            .apply {
                adapter = this
            }

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edSearch.text.toString().isNotEmpty() && !tvSearchStatus) {
                    TransitionManager.beginDelayedTransition(clSearch)
                    ConstraintSet().apply {
                        clone(clSearch)
                        clear(tvSearch.id, ConstraintSet.START)  //擦除Start
                        connect(tvSearch.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                        applyTo(clSearch)
                    }
                    tvSearchStatus = true
                } else if (edSearch.text.toString().isEmpty() && tvSearchStatus) {
                    TransitionManager.beginDelayedTransition(clSearch)
                    ConstraintSet().apply {
                        clone(clSearch)
                        clear(tvSearch.id, ConstraintSet.END)  //擦除END
                        connect(tvSearch.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.END)
                        applyTo(clSearch)
                    }
                    tvSearchStatus = false
                }
                Inputtips(this@SearchActivity,
                    InputtipsQuery(edSearch.text.toString(), FuckApplication.cityCode)
                        .apply {
                            cityLimit = true //限制为当前城市
                        })
                    .apply {
                        setInputtipsListener(this@SearchActivity)
                    }.requestInputtipsAsyn()

            }
        })
    }


    override fun onGetInputtips(tipList: MutableList<Tip>?, p1: Int) {
        items.clear()
        tipList?.let {
            it.forEach { tip ->
                items.add(SearchMultiItemBean(0, tip))
            }
        }
        adapter.setNewData(items)
    }

}