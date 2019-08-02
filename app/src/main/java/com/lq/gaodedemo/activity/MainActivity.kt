package com.lq.gaodedemo.activity

import android.Manifest
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.MyLocationStyle
import com.gyf.immersionbar.ImmersionBar
import com.lq.gaodedemo.FuckApplication
import com.lq.gaodedemo.LocationUtils
import com.lq.gaodedemo.R
import com.lq.gaodedemo.pop.MainEditLocationPop
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), AMapLocationListener {

    private lateinit var aMap: AMap
    private lateinit var myLocationStyle: MyLocationStyle
    private lateinit var uiSettings: UiSettings
    private lateinit var disposable: Disposable
    private lateinit var mainEditLocationPop: MainEditLocationPop
    private lateinit var mLocationUtils: LocationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.exitTransition = Explode()
        setContentView(R.layout.activity_main)

        ImmersionBar
            .with(this)
            .statusBarDarkFont(true)
            .fullScreen(true)
            .transparentStatusBar()
            .navigationBarColor(android.R.color.black)
            .init()

        mainEditLocationPop = MainEditLocationPop(this)
        mapView.onCreate(savedInstanceState)
        initMap()
        requestPermissions()
        addListener()
    }

    private fun addListener() {

        button.setOnClickListener {
            ActivityCompat.startActivity(
                this, Intent(this, RouteSearchActivity::class.java),
                ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            )
        }

        layoutLocation.postDelayed({
            uiSettings.setLogoBottomMargin(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16.0f,
                    resources.displayMetrics
                ).toInt() + constraintLayout2.height
            )
            uiSettings.setLogoLeftMargin(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    8.0f,
                    resources.displayMetrics
                ).toInt() + layoutLocation.width
            )
        }, 300)

        layoutEdit.setOnClickListener {
            val ints = intArrayOf(0, 0)
            layoutEdit.getLocationInWindow(ints)
            alphaAni(1.0f, 0.5f)
            mainEditLocationPop.showAtLocation(
                layoutEdit, Gravity.TOP or Gravity.RIGHT,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, resources.displayMetrics).toInt(),
                ints[1] + layoutEdit.height - mainEditLocationPop.getPopHeight()
            )
        }
        mainEditLocationPop.setOnDismissListener {
            alphaAni(0.5f, 1.0f)
        }

        layoutSms.setOnClickListener {
            startActivity<SmsActivity>()
        }

        constraintLayout2.setOnClickListener {
            startActivity<NearbyActivity>()
        }

        clTopSearch.setOnClickListener {
            startActivity<SearchActivity>()
        }

    }

    private fun alphaAni(start: Float, end: Float) {
        val alpha = ValueAnimator.ofFloat(start, end)
        alpha.duration = 500
        alpha.addUpdateListener {
            val params = window.attributes
            params.alpha = it.animatedValue as Float
            window.attributes = params
        }
        alpha.start()
    }


    private fun requestPermissions() {
        val permissions = RxPermissions(this)
        permissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
        )
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {
                    disposable.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        Log.e("权限申请成功", "$t")
                        aMap.isMyLocationEnabled = true
                        mLocationUtils.startLocation()
                    } else {
                        Log.e("权限申请组至少一个失败了", "$t")
                    }
                    disposable.dispose()
                }

                override fun onError(e: Throwable) {
                    disposable.dispose()
                }
            })
    }

    private fun initMap() {
        aMap = mapView.map
        uiSettings = aMap.uiSettings
        uiSettings.isZoomControlsEnabled = false
        myLocationStyle = MyLocationStyle()
        myLocationStyle.strokeColor(Color.parseColor("#00000000"))
        myLocationStyle.radiusFillColor(Color.parseColor("#2f0098FF"))
        myLocationStyle.strokeWidth(10.0f)
        aMap.myLocationStyle = myLocationStyle
        mLocationUtils = LocationUtils(true, this)
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
        mLocationUtils.onDestory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation == null) {
            toast("定位失败!!!")
        } else {
            if (aMapLocation.errorCode == 0) {
                FuckApplication.cityName = aMapLocation.city
                FuckApplication.cityCode = aMapLocation.cityCode
                toast(FuckApplication.cityName)
                aMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f)) //移动缩放 3-20
            }
        }
    }
}


