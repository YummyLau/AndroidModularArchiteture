package com.effective.android.permission

import android.Manifest
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.permission_activity_main.*

class TestActivity : AppCompatActivity() {

    private var permissionManager: PermissionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager.injectPermission(this)
        permissionManager?.setLogging(true)

        setContentView(R.layout.permission_activity_main)

        onlyCamera.setOnClickListener {
            permissionManager?.request(object : ResultCall {

                override fun granted() {
                    Log.d(TAG, "Permission granted[camera]")
                }

                override fun denied(never: Boolean) {
                    if (never) {
                        Log.d(TAG, "Permission denied, can't enable the camera")
                    } else {
                        Log.d(TAG, "Denied permission without ask never again")
                    }

                }
            }, Manifest.permission.CAMERA)
        }

        cameraAndNet.setOnClickListener {
            permissionManager?.request(object : ResultCall {

                override fun granted() {
                    Log.d(TAG, "Permission granted[camera,net]")
                }

                override fun denied(never: Boolean) {
                    if (never) {
                        Log.d(TAG, "Permission denied,can't enable the camera or contacts")
                    } else {
                        Log.d(TAG, "Denied permission,without ask camera or contacts never again")
                    }
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS)
        }

        cameraAndNetCombined.setOnClickListener {
            permissionManager?.request(object : MutilResultCall {

                override fun granted(permission: String) {
                    Log.d(TAG, "Permission granted[ $permission ]")
                }

                override fun denied(permission: String, never: Boolean) {
                    if (never) {
                        Log.d(TAG, "Permission denied, can't enable the $permission")
                    } else {
                        Log.d(TAG, "Denied permission without ask $permission never again")
                    }
                }
            }, Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        permissionManager?.destroy()
    }


    companion object {
        private val TAG = "permissionManager"
    }
}
