package com.lq.gaodedemo.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.transition.ChangeBounds
import android.transition.Explode
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.lq.gaodedemo.DensityUtils
import com.lq.gaodedemo.R
import com.lq.gaodedemo.pop.RouteSearchMorePop
import com.lq.gaodedemo.fragment.SearchFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_route_search.*
import java.util.concurrent.TimeUnit

/**
 * Created by liuqian on 2019/2/27 11:26
 * Describe:
 */
class RouteSearchActivity : AppCompatActivity() {

    private lateinit var routeSearchMorePop: RouteSearchMorePop
    private var edOrder: Boolean = false //是否需要调换顺序

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val explode = Explode()
        explode.excludeTarget(android.R.id.statusBarBackground, true)
        explode.duration = 500
        window.enterTransition = explode
        setContentView(R.layout.activity_route_search)
        initEdit()
        initTab()
        addListener()
        startScene()
    }

    /**
     * 初始化 相关
     */
    private fun initEdit() {
        routeSearchMorePop = RouteSearchMorePop(this)
        linearLayout.addView(createEditView("我的位置", "输入起点", R.id.create_edit1))
        linearLayout.addView(createLine())
        linearLayout.addView(createEditView("", "输入终点", R.id.create_edit2))
    }

    private fun initTab() {
        val viewList = ArrayList<SearchFragment>()
        for (item in RouteSearchMorePop.listStr) {
            tabLayout.addTab(tabLayout.newTab().setText(item))
            viewList.add(SearchFragment())
        }
        tabLayout.setupWithViewPager(viewPager)
        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(p0: Int): Fragment {
                return viewList[p0]
            }

            override fun getCount(): Int {
                return viewList.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return RouteSearchMorePop.listStr[position]
            }
        }
        viewPager.adapter = adapter
    }

    /**
     * 启动过渡
     */
    private fun startScene() {
        val viewEdit = linearLayout.getChildAt(2) as EditText
        viewEdit.translationY = -100.0f
        viewEdit.alpha = 0.0f
        val dis = Observable.timer(300, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            circleChangeBoundsView.startDraw()
            val dis2 =
                    Observable.timer(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                        val animationTranslation = ObjectAnimator.ofFloat(viewEdit, "translationY", -100.0f, 0.0f)
                        val animationAlpha = ObjectAnimator.ofFloat(viewEdit, "alpha", 0.0f, 1.0f)
                        val set = AnimatorSet()
                        set.duration = 1000
                        set.playTogether(animationTranslation, animationAlpha)
                        set.start()
                    }
        }
    }


    /**
     * 添加监听
     */
    private fun addListener() {
        img_import.setOnClickListener {
            addEditView()
        }
        img_back.setOnClickListener {
            onBackPressed()
        }
        more.setOnClickListener {
            var ints = intArrayOf(0, 0)
            more.getLocationInWindow(ints)
            routeSearchMorePop.showAtLocation(more, Gravity.TOP or Gravity.RIGHT,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1.0f, resources.displayMetrics).toInt(),
                    ints[1] - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7.0f, resources.displayMetrics).toInt())
        }
    }


    /**
     * 过渡动画 过程
     */
    private fun addEditView() {
        TransitionManager.beginDelayedTransition(linearLayout, ChangeBounds())
        edOrder = if (edOrder) {
            linearLayout.removeAllViews()
            linearLayout.addView(createEditView("我的位置", "输入起点", R.id.create_edit1))
            linearLayout.addView(createLine())
            linearLayout.addView(createEditView("", "输入终点", R.id.create_edit2))
            false
        } else {
            linearLayout.removeAllViews()
            linearLayout.addView(createEditView("", "输入起点", R.id.create_edit2))
            linearLayout.addView(createLine())
            linearLayout.addView(createEditView("我的位置", "输入终点", R.id.create_edit1))
            true
        }
    }

    /**
     * 创建分割线
     */
    private fun createLine(): View {
        val view = View(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                DensityUtils.dp2px(this, 0.5f)
        )
        params.bottomMargin = DensityUtils.dp2px(this, 4.0f)
        params.topMargin = DensityUtils.dp2px(this, 4.0f)
        view.layoutParams = params
        view.setBackgroundColor(Color.parseColor("#bdbdbd"))
        return view

    }

    /**
     * 创建输入框
     */
    private fun createEditView(text: String, hint: String, @IdRes id: Int): EditText {
        val edittext = EditText(this)
        edittext.id = id
        edittext.textSize = 16.0f
        edittext.setTextColor(Color.BLACK)
        edittext.setHintTextColor(Color.parseColor("#424242"))
        edittext.setSingleLine(true)
        edittext.hint = hint
        edittext.setText(text)
        edittext.background = null
        val params =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        edittext.layoutParams = params
        val padding = DensityUtils.dp2px(this, 8.0f)
        edittext.setPadding(0, padding, padding, padding)
        return edittext
    }
}