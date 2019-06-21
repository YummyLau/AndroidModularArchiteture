package com.effective.android.geo

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView

import androidx.appcompat.app.AppCompatActivity
import com.effective.android.geo.location.LocationActivity

import java.util.ArrayList

/***
 * demo入口，用于配置信息，参看官网项目
 */
class MainActivity : AppCompatActivity() {
    private val SDK_PERMISSION_REQUEST = 127
    private var FunctionList: ListView? = null
    private var permissionInfo: String? = null

    private val data: List<String>
        get() {
            val data = ArrayList<String>()
            data.add("基础定位功能")
            data.add("配置定位参数")
            data.add("自定义回调示例")
            data.add("连续定位示例")
            data.add("位置消息提醒")
            data.add("室内定位功能")
            data.add("判断移动热点")
            data.add("android 8.0后台定位示例")
            data.add("常见问题说明")
            return data
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.function_list)
        FunctionList = findViewById<View>(R.id.functionList) as ListView
        FunctionList!!.adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, data)
        // after andrioid m,must request Permiision on runtime
        getPersimmions()
    }

    @TargetApi(23)
    private fun getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = ArrayList<String>()
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            /*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n"
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n"
            }

            if (permissions.size > 0) {
                requestPermissions(permissions.toTypedArray(), SDK_PERMISSION_REQUEST)
            }
        }
    }

    @TargetApi(23)
    private fun addPermission(permissionsList: ArrayList<String>, permission: String): Boolean {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true
            } else {
                permissionsList.add(permission)
                return false
            }

        } else {
            return true
        }
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    override fun onStart() {
        super.onStart()
        FunctionList!!.onItemClickListener = OnItemClickListener { arg0, arg1, arg2, arg3 ->
            var TargetClass: Class<*>? = null
            when (arg2) {
                0 -> TargetClass = LocationActivity::class.java
                1 -> TargetClass = LocationOption::class.java
                2 -> TargetClass = LocationAutoNotify::class.java
                3 -> TargetClass = LocationFilter::class.java
                4 -> TargetClass = NotifyActivity::class.java
                5 -> TargetClass = IndoorLocationActivity::class.java
                6 -> TargetClass = IsHotWifiActivity::class.java
                7 -> TargetClass = ForegroundActivity::class.java
                8 -> TargetClass = QuestActivity::class.java
                else -> {
                }
            }
            if (TargetClass != null) {
                val intent = Intent(this@MainActivity, TargetClass)
                intent.putExtra("from", 0)
                startActivity(intent)
            }
        }
    }
}
