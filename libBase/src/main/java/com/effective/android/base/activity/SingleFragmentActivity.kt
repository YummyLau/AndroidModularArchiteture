package com.effective.android.base.activity

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import com.effective.android.base.R
import com.effective.android.base.fragment.BaseFragment


/**
 * Created by yummyLau on 2018/4/26.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
abstract class SingleFragmentActivity : AppCompatActivity(){

    abstract fun createFragment(): BaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_single_fragment_layout)
        val fm = supportFragmentManager
        var fragment: BaseFragment? = fm.findFragmentById(R.id.id_fragment_container) as BaseFragment
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction().add(R.id.id_fragment_container, fragment).commit()
        }
    }

    @NonNull
    override fun getDelegate(): AppCompatDelegate {
        return SkinAppCompatDelegateImpl.get(this, this)
    }
}