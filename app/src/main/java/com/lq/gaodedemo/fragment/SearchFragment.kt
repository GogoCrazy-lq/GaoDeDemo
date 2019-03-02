package com.lq.gaodedemo.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lq.gaodedemo.R

/**
 * Created by liuqian on 2019/2/28 17:52
 * Describe:
 */
class SearchFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search , container , false)
    }

}