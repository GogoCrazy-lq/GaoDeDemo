package com.lq.gaodedemo

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener


class LocationUtils(private val isDoubleLocation: Boolean, listener: AMapLocationListener) {

    //声明AMapLocationClient类对象
    private var mLocationClient: AMapLocationClient? = null
    //定位相关参数
    private var mLocationOptional: AMapLocationClientOption? = null

    init {
        mLocationClient = AMapLocationClient(FuckApplication.context).apply {
            setLocationListener(listener)
        }
        initOption()
    }

    /**
     * 默认连续定位的
     */
    private fun initOption() {
        mLocationOptional = AMapLocationClientOption()
        //确定定位场景 签到 出行 运动
        mLocationOptional!!.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
        mLocationOptional!!.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        mLocationOptional!!.isOnceLocation = isDoubleLocation
        mLocationOptional!!.interval = 2000
        mLocationOptional!!.isMockEnable = true //返回地址信息
        mLocationClient?.setLocationOption(mLocationOptional)
    }

     fun startLocation() {
        mLocationClient?.stopLocation()
        mLocationClient?.startLocation()
    }

     fun stopLocation() {
        mLocationClient?.stopLocation()
    }

    fun onDestory(){
        mLocationClient?.onDestroy()
    }

}