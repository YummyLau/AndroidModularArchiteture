package com.effective.android.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.effective.android.base.fragment.BaseFragment
import com.effective.android.base.R

/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class SingleFragmentActivity : AppCompatActivity(){

    abstract fun createFragment(): BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment_layout)
        val fm = supportFragmentManager
        var fragment: BaseFragment? = fm.findFragmentById(R.id.id_fragment_container) as BaseFragment
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction().add(R.id.id_fragment_container, fragment).commit()
        }
    }
}