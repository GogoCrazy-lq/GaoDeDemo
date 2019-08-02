package com.lq.gaodedemo.adapter

import com.amap.api.services.core.PoiItem
import com.amap.api.services.help.Tip
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.gaodedemo.R
import com.lq.gaodedemo.bean.SearchMultiItemBean


class SearchAdapter(listData: ArrayList<SearchMultiItemBean<*>>) : BaseMultiItemQuickAdapter<SearchMultiItemBean<*>, BaseViewHolder>(listData) {

    init {
        addItemType(0, R.layout.item_search_list1)
        addItemType(1, R.layout.item_search_list1)
        addItemType(2, R.layout.item_search_list1)
    }

    override fun convert(helper: BaseViewHolder, item: SearchMultiItemBean<*>) {
        when (item.type) {
            0 -> { //
                val tip = item.t as Tip
                helper.setText(R.id.textView, "${tip.name} ${tip.district}")
            }
            1 -> {
                val poiItem = item.t as PoiItem
                helper.setText(R.id.textView, "${poiItem.adName} ${poiItem.cityName}")
            }
            2 -> {
                val string = item.t as String
                helper.setText(R.id.textView, "推荐搜索：$string")
            }
        }
    }

}