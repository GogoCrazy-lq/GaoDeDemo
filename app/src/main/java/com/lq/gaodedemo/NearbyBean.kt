package com.lq.gaodedemo

import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import kotlinx.android.parcel.Parcelize

/**
 * Created by liuqian on 2019/3/4 16:56
 * Describe:
 */
@Parcelize
data class NearbyBean(var title: String = "", var type: Int = 0) : Parcelable, MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}