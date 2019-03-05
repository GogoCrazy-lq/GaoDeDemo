package com.lq.gaodedemo.adapter

import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lq.gaodedemo.DensityUtils
import com.lq.gaodedemo.NearbyBean
import com.lq.gaodedemo.R
import com.lq.gaodedemo.pop.RouteSearchMorePop

/**
 * Created by liuqian on 2019/3/4 14:34
 * Describe:
 */
class NearbyAdapter(listData: ArrayList<NearbyBean>) : BaseMultiItemQuickAdapter<NearbyBean, BaseViewHolder>(listData) {


    init {
        addItemType(0, R.layout.item_nearby_list0)
        addItemType(1, R.layout.item_nearby_list)
        addItemType(2, R.layout.item_nearby_list1)
    }

    override fun convert(helper: BaseViewHolder, item: NearbyBean) {
        when (helper.itemViewType) {
            0 -> {

                val size = mContext.resources.displayMetrics.widthPixels / 5
                val parent = helper.getView<LinearLayout>(R.id.parent)
                val params = ViewGroup.LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                parent.gravity = Gravity.CENTER
                parent.layoutParams = params
                helper.setText(R.id.textView, item.title)
                helper.setImageDrawable(
                        R.id.imageView,
                        mContext.resources.getDrawable(RouteSearchMorePop.moreType[(Math.random() * 10).toInt()])
                )
            }
            1 -> {
                helper.setText(R.id.textView, item.title)
            }
            2 -> {
                val margin = DensityUtils.dp2px(mContext, 6.0f)
                val size = (mContext.resources.displayMetrics.widthPixels - DensityUtils.dp2px(mContext, 16.0f) * 2 - margin) / 2


                val cardView1 = helper.getView<CardView>(R.id.cardView1)
                val cardView2 = helper.getView<CardView>(R.id.cardView2)
                val cardView3 = helper.getView<CardView>(R.id.cardView3)
                val cardView4 = helper.getView<CardView>(R.id.cardView4)
                val cardView5 = helper.getView<CardView>(R.id.cardView5)

                val paramsCard1 = RelativeLayout.LayoutParams(size, size)
                cardView1.layoutParams = paramsCard1

                val paramsCard2 = RelativeLayout.LayoutParams(size, (size - margin) / 2)
                paramsCard2.addRule(RelativeLayout.RIGHT_OF, R.id.cardView1)
                paramsCard2.setMargins(margin, 0, 0, 0)
                cardView2.layoutParams = paramsCard2

                val paramsCard3 = RelativeLayout.LayoutParams(size, (size - margin) / 2)
                paramsCard3.addRule(RelativeLayout.RIGHT_OF, R.id.cardView1)
                paramsCard3.addRule(RelativeLayout.BELOW, R.id.cardView2)
                paramsCard3.setMargins(margin, margin, 0, 0)
                cardView3.layoutParams = paramsCard3


                val paramsCard4 = RelativeLayout.LayoutParams(size, (size - margin) / 2)
                paramsCard4.addRule(RelativeLayout.BELOW, R.id.cardView1)
                paramsCard4.setMargins(0, margin, margin, 0)
                cardView4.layoutParams = paramsCard4

                val paramsCard5 = RelativeLayout.LayoutParams(size, (size - margin) / 2)
                paramsCard5.addRule(RelativeLayout.BELOW, R.id.cardView1)
                paramsCard5.addRule(RelativeLayout.RIGHT_OF, R.id.cardView4)
                paramsCard5.setMargins(0, margin, 0, 0)
                cardView5.layoutParams = paramsCard5
            }
        }
    }


}