package com.effective.android.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions

import java.util.ArrayList

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * 兼容android全版本权限请求
 * created by yummylau on 2019/09/04
 */
class PermissionManager(private val rxPermissions: RxPermissions) {

    private val disposables: MutableList<Disposable>

    init {
        this.disposables = ArrayList()
    }

    fun setLogging(enable: Boolean) {
        rxPermissions.setLogging(enable)
    }

    /**
     * 传递单一权限判断
     *
     * @param resultCall
     * @param permission example Manifest.permission.CAMERA
     */
    fun request(resultCall: ResultCall, permission: String) {
        disposables.add(rxPermissions.requestEach(permission)
                .subscribe({ permission ->
                    if (permission.granted) {
                        resultCall.granted()
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        resultCall.denied(false)
                    } else {
                        resultCall.denied(true)
                    }
                }, { }))
    }

    /**
     * 传递多个权限判断，结果只回调一次
     *
     * @param resultCall  void granted() 当且所有权限都通过时调用
     * void denied(boolean never) never为false时表示至少已有一个权限没有勾选不再询问被拒绝，never为ture表示至少有一个权限被勾选不再询问被拒绝
     * @param permissions
     */
    fun request(resultCall: ResultCall, vararg permissions: String) {
        disposables.add(rxPermissions.requestEachCombined(*permissions)
                .subscribe({ permission ->
                    if (permission.granted) {
                        resultCall.granted()
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        resultCall.denied(false)
                    } else {
                        resultCall.denied(true)
                    }
                }, { }))
    }


    /**
     * 传递多个权限判断，结果回调多次
     *
     * @param mutilResultCall void granted(String permission); permission可以通过传递参数判断对应哪个权限通过
     * void denied(String permission, boolean never);  permission可以通过传递参数判断对应哪个权限被拒绝
     * @param permissions     传入n个权限，则resultCall会回调n次
     */
    fun request(mutilResultCall: MutilResultCall, vararg permissions: String) {
        disposables.add(rxPermissions.requestEach(*permissions)
                .subscribe({ permission ->
                    if (permission.granted) {
                        mutilResultCall.granted(permission.name)
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        mutilResultCall.denied(permission.name, false)
                    } else {
                        mutilResultCall.denied(permission.name, true)
                    }
                }, { }))
    }


    fun destroy() {
        for (disposable in disposables) {
            if (disposable != null && !disposable.isDisposed) {
                disposable.dispose()
            }
        }
    }

    companion object {

        fun injectPermission(activity: FragmentActivity): PermissionManager {
            return PermissionManager(RxPermissions(activity))
        }

        fun injectPermission(fragment: Fragment): PermissionManager {
            return PermissionManager(RxPermissions(fragment))
        }
    }
}
