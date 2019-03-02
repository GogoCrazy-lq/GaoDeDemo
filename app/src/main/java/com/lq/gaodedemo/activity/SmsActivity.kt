package com.lq.gaodedemo.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.lq.gaodedemo.R
import com.lq.gaodedemo.adapter.SmsAdapter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_sms.*
import java.util.concurrent.TimeUnit

/**
 * Created by liuqian on 2019/3/1 11:35
 * Describe:
 */

class SmsActivity : AppCompatActivity() {

    private lateinit var disposable: Disposable

    private lateinit var smsAdapter: SmsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)

        toolbar.title = "举头望明月,明月照他乡"
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_chevron_left_white_28dp)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        smsAdapter = SmsAdapter(R.layout.item_sms_list)
        recyclerView.adapter = smsAdapter

        val dis = Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    loadingView.startAni()
                    Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Observer<Long> {
                                override fun onComplete() {
                                }

                                override fun onSubscribe(d: Disposable) {
                                    disposable = d
                                }

                                override fun onNext(t: Long) {
                                    if (t == 3L) {
                                        setData()
                                        loadingView.closeAni()
                                        disposable.dispose()
                                    }
                                }

                                override fun onError(e: Throwable) {
                                }
                            })
                }
    }

    private fun setData() {
        val listStr = ArrayList<String>()
        for (item in 0..15) {
            listStr.add("$item")
        }
        smsAdapter.setNewData(listStr)
    }
}