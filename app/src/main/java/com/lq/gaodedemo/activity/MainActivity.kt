package com.lq.gaodedemo.activity

import android.Manifest
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.Explode
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.widget.Toast
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.UiSettings
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.maps.model.PolylineOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.DRIVING_SINGLE_SHORTEST
import com.lq.gaodedemo.pop.MainEditLocationPop
import com.lq.gaodedemo.R
import com.lq.gaodedemo.pop.SearchResultPop
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PoiSearch.OnPoiSearchListener,
        SearchResultPop.OnSearchResultClickListener,
        RouteSearch.OnRouteSearchListener {

    private lateinit var aMap: AMap

    private lateinit var myLocationStyle: MyLocationStyle

    private lateinit var uiSettings: UiSettings

    private lateinit var disposable: Disposable

    private lateinit var query: PoiSearch.Query
    private lateinit var poiSearch: PoiSearch

    private lateinit var searchResultPop: SearchResultPop
    private lateinit var mainEditLocationPop: MainEditLocationPop

    private var listLatLng = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.exitTransition = Explode()

        setContentView(R.layout.activity_main)

        toolbar.title = "窗前明月光,14地上霜。"
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        search_view.setHint("搜索目标位置")
        search_view.setVoiceSearch(true) //or false

        searchResultPop = SearchResultPop(this)
        searchResultPop.setResultClickListener(this)

        mainEditLocationPop = MainEditLocationPop(this)

        mapView.onCreate(savedInstanceState)
        initMap()
        requestPermisssions()
        addListener()
    }

    private fun addListener() {
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.e("searchView_state", "onQueryTextChange")
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e("searchView_state", "onQueryTextSubmit")
                getPOIData(query)
                return true
            }
        })

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                Log.e("searchView_state", "onSearchViewClosed")
            }

            override fun onSearchViewShown() {
                Log.e("searchView_state", "onSearchViewShown")
            }
        })

        button.setOnClickListener {
            ActivityCompat.startActivity(this, Intent(this, RouteSearchActivity::class.java),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
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
            var ints = intArrayOf(0, 0)
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
            startActivity(Intent(this, SmsActivity::class.java))
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


    private fun requestPermisssions() {
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
        myLocationStyle.interval(2000)
        myLocationStyle.strokeColor(Color.parseColor("#00000000"))
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.radiusFillColor(Color.parseColor("#2f0098FF"))
        myLocationStyle.strokeWidth(10.0f)
        aMap.myLocationStyle = myLocationStyle
        aMap.showIndoorMap(true)

        aMap.setOnMyLocationChangeListener {
            val latLng = LatLng(it.latitude, it.longitude)
            if (listLatLng.size == 0) {
                listLatLng.add(latLng)
            } else {
                listLatLng[0] = latLng
            }

            aMap.moveCamera(CameraUpdateFactory.zoomTo(16.0f))
            Toast.makeText(this, "${it.latitude} , ${it.longitude}", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        search_view.setMenuItem(menu?.findItem(R.id.search))
        return true

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ((requestCode == MaterialSearchView.REQUEST_VOICE) and (resultCode == RESULT_OK)) {
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if ((matches != null) and (matches?.size!! > 0)) {
                val searchWrd = matches[0]
                if (!TextUtils.isEmpty(searchWrd)) {
                    search_view.setQuery(searchWrd, false)
                    Toast.makeText(this, searchWrd, Toast.LENGTH_LONG).show()
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //设置搜索条件
    private val pageNum = 1

    private fun getPOIData(searchKey: String?, cityCode: String = "") {
        query = PoiSearch.Query(searchKey, cityCode)
        query.pageSize = 10
        query.pageNum = pageNum

        poiSearch = PoiSearch(this, query)
        poiSearch.setOnPoiSearchListener(this)

        poiSearch.searchPOIAsyn()
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {
        //id检索结果
    }

    private var listItem: List<PoiItem>? = null
    private var listStr: ArrayList<String>? = null
    override fun onPoiSearched(result: PoiResult?, code: Int) {
        //查询到的结果
        listStr = ArrayList()
        if (code != 1000) return
        listItem = result?.pois
        if (listItem == null) return
        searchResultPop.setPoiData(listItem!!)
        searchResultPop.showAsDropDown(toolbar, 0, 0)
    }


    override fun onClickItem(position: Int, title: String, poiItem: PoiItem) {
        //点击完成后
        searchResultPop.dismiss()
        if (search_view.isSearchOpen)
            search_view.closeSearch()
        //准备绘制点标记addMarker 计算距离
        val latLng = LatLng(poiItem.latLonPoint.latitude, poiItem.latLonPoint.longitude)
        val marker = aMap.addMarker(MarkerOptions().position(latLng).title(title).snippet(poiItem.snippet))
        listLatLng.add(latLng)
        aMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        //绘制直线
        aMap.addPolyline(PolylineOptions().addAll(listLatLng).width(5.0f).color(Color.RED))

//        //规划路线开始
//        routeSearch()
    }

    private var routeSearch: RouteSearch? = null
    private fun routeSearch() {
        routeSearch = RouteSearch(this)
        routeSearch!!.setRouteSearchListener(this)

        var fromAndTo = RouteSearch.FromAndTo(
                LatLonPoint(listLatLng[0].latitude, listLatLng[0].longitude),
                LatLonPoint(listLatLng[1].latitude, listLatLng[1].longitude)
        )
        val dirveRouteQuery = RouteSearch.DriveRouteQuery(
                fromAndTo,
                DRIVING_SINGLE_SHORTEST, null, null, ""
        )
        routeSearch!!.calculateDriveRoute(dirveRouteQuery)
    }

    override fun onDriveRouteSearched(result: DriveRouteResult?, code: Int) {
        if (code != 1000) return
        if (result != null) {
            val drivePath = result.paths
            drivePath[0].steps
        }
    }

    override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
    }

    override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
    }

    override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
    }
}


