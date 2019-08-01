package com.lq.gaodedemo.activity

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.view.View
import com.amap.api.services.core.PoiItem
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.gyf.immersionbar.ImmersionBar
import com.lq.gaodedemo.FuckApplication
import com.lq.gaodedemo.R
import com.lq.gaodedemo.adapter.SearchAdapter
import com.lq.gaodedemo.bean.SearchMultiItemBean
import kotlinx.android.synthetic.main.activity_search.*

/**
 * POI搜索
 * 实现 输入内容自动提示
 */
class SearchActivity : AppCompatActivity(), PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener {

    private var currentPage = 1
    private var searchKey: String = ""

    private val items = ArrayList<SearchMultiItemBean<*>>()

    private lateinit var adapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ImmersionBar.with(this).statusBarDarkFont(true).statusBarColor(android.R.color.white)
                .navigationBarColor(android.R.color.black).init()
        initSet()
        setListener()
    }

    private fun initSet() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity, RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@SearchActivity, RecyclerView.VERTICAL).apply {
                setDrawable(ContextCompat.getDrawable(this@SearchActivity, R.drawable.shape_divider)!!)
            })
        }.adapter = SearchAdapter(items).apply {
            adapter = this
        }

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (tvSearch.visibility == View.GONE && edSearch.text.toString().isNotEmpty()) {
                    TransitionManager.beginDelayedTransition(clSearch)
                    tvSearch.visibility = View.VISIBLE
                } else if (tvSearch.visibility == View.VISIBLE && edSearch.text.toString().isEmpty()) {
                    TransitionManager.beginDelayedTransition(clSearch)
                    tvSearch.visibility = View.GONE
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

    private fun setListener() {
        tvSearch.setOnClickListener {
            startSearch()
        }
    }

    /**
     * 手动搜索
     */
    private fun startSearch() {
        //关键字、搜索的POI类型(默认“餐饮服务”、“商务住宅”、“生活服务”)、城市代码（“” 表示全国、可以城市名称、城市编码）
        PoiSearch(this, PoiSearch.Query(searchKey, "", FuckApplication.cityCode)
                .apply {
                    pageSize = 10
                    pageNum = currentPage
                })
                .apply { setOnPoiSearchListener(this@SearchActivity) }
                .searchPOIAsyn()
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
        //id检索
    }

    override fun onPoiSearched(result: PoiResult, code: Int) {
        //搜索结果
        if (code != 1000) {
            //推荐搜索
            items.clear()
            val suggestSearchKeyList = result.searchSuggestionKeywords
            suggestSearchKeyList.forEach {
                items.add(SearchMultiItemBean(2, it))
            }
            adapter.setNewData(items)
        } else {
            if (currentPage == 1) {
                items.clear()
            }
            val poiItemList = result.pois
            if (poiItemList.isNullOrEmpty()) {
                //查询不到 获取建议城市
                val suggestCityList = result.searchSuggestionCitys

                suggestCityList.forEach {
                    items.add(SearchMultiItemBean(1, it))
                }
                adapter.setNewData(items)
            }
        }

    }

    override fun onGetInputtips(tipList: MutableList<Tip>?, p1: Int) {
        items.clear()
        if (!tipList.isNullOrEmpty()) {
            tipList.forEach {
                items.add(SearchMultiItemBean(0, it))
            }
            adapter.setNewData(items)
        }
    }

}