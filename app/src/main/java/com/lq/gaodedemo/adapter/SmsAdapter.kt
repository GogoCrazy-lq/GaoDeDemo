package com.lq.gaodedemo.adapter

import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.gaodedemo.R

/**
 * Created by liuqian on 2019/3/1 14:19
 * Describe:
 */

class SmsAdapter constructor(@LayoutRes layoutResId: Int) :
        BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {

    override fun convert(helper: BaseViewHolder, item: String) {
        val size = mContext.resources.displayMetrics.widthPixels / 3
        val image = helper.getView<ImageView>(R.id.image)
        val params = LinearLayout.LayoutParams(size, size * 2 / 3)
        image.layoutParams = params
    }

}