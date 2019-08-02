package com.lq.gaodedemo.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.help.Tip
import com.gyf.immersionbar.ImmersionBar
import com.lq.gaodedemo.R
import kotlinx.android.synthetic.main.activity_aims_map_detail.*

/**
 * 目标位置详情页面
 */
class AimsMapDetailActivity : AppCompatActivity() {

    private lateinit var aMap: AMap
    private lateinit var uiSettings: UiSettings
    private lateinit var tip : Tip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aims_map_detail)
        ImmersionBar
                .with(this)
                .statusBarDarkFont(true)
                .fullScreen(true)
                .transparentStatusBar()
                .navigationBarColor(android.R.color.black)
                .init()
        mapView.onCreate(savedInstanceState)
        initSet()
        initMap()
    }

    private fun initSet() {
        tip = intent?.getParcelableExtra("tip") as Tip
        tvAnims.text = tip.name
        clBottomView.postDelayed({
            uiSettings.setLogoBottomMargin(
                    TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            130.0f,
                            resources.displayMetrics
                    ).toInt()
            )
        }, 300)
    }

    /**
     * 初始化地图
     */
    private fun initMap() {
        aMap = mapView.map
        uiSettings = aMap.uiSettings
        uiSettings.isZoomControlsEnabled = false
        val point = tip.point
        aMap.addMarker(MarkerOptions().apply {
            position(LatLng(point.latitude, point.longitude))
            icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        })
        val cameraUpdate = CameraUpdateFactory.changeLatLng(LatLng(point.latitude, point.longitude))
        aMap.moveCamera(cameraUpdate)
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f)) //移动缩放 3-20
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }


}