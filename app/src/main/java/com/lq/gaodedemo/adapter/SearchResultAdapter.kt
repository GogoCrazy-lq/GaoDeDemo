package com.lq.gaodedemo.adapter

import android.support.annotation.LayoutRes
import com.amap.api.services.core.PoiItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.gaodedemo.R

/**
 * Created by liuqian on 2019/2/26 17:35
 * Describe:
 */
class SearchResultAdapter constructor(@LayoutRes layoutResId : Int) : BaseQuickAdapter<PoiItem, BaseViewHolder>(layoutResId){

    override fun convert(helper: BaseViewHolder?, item: PoiItem) {
        helper!!.setText(R.id.textView, item.title)
    }

}