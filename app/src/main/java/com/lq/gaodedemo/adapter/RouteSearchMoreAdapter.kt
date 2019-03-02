package com.lq.gaodedemo.adapter

import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.gaodedemo.R
import com.lq.gaodedemo.pop.RouteSearchMorePop

/**
 * Created by liuqian on 2019/2/26 17:35
 * Describe:
 */
class RouteSearchMoreAdapter constructor(@LayoutRes layoutResId: Int) :
    BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: String) {

        val size = mContext.resources.displayMetrics.widthPixels / 5 * 3

        val parent = helper.getView<ConstraintLayout>(R.id.parent)

        val params = ViewGroup.LayoutParams(size / 3, ViewGroup.LayoutParams.WRAP_CONTENT)
        parent.layoutParams = params

        helper.setText(R.id.textView, item)

        helper.setImageDrawable(
            R.id.imageView,
            mContext.resources.getDrawable(RouteSearchMorePop.moreType[RouteSearchMorePop.listStr.indexOf(item)])
        )
    }

}