package com.effective.android.webview.runalone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.ValueCallback
import android.widget.Button
import com.effective.android.webview.R
import com.effective.android.webview.X5JsWebView
import com.effective.android.webview.interfaces.BridgeHandler
import com.effective.android.webview.interfaces.CallBackFunction
import com.effective.android.webview.jsbridge.DefaultHandler
import com.google.gson.Gson
import com.tencent.smtt.sdk.WebChromeClient

class TestWebViewActivity : Activity() {

    private val TAG = "MainActivity"

    internal var RESULT_CODE = 0
    internal var mUploadMessage: ValueCallback<Uri>? = null

    internal class Location {
        var address: String? = null
    }

    internal class User {
        var name: String? = null
        var location: Location? = null
        var testStr: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_test)
        val webView = findViewById<View>(R.id.webView) as X5JsWebView
        val button = findViewById<View>(R.id.button) as Button

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                webView.callHandler("functionInJs", "data from Java", object : CallBackFunction {

                    override fun onCallBack(data: String?) {
                        Log.i(TAG, "reponse data from js $data")
                    }
                })
            }
        })

        webView.setDefaultHandler(DefaultHandler())
        webView.setWebChromeClient(object : WebChromeClient() {

            fun openFileChooser(uploadMsg: ValueCallback<Uri>, AcceptType: String, capture: String) {
                this.openFileChooser(uploadMsg)
            }

            fun openFileChooser(uploadMsg: ValueCallback<Uri>, AcceptType: String) {
                this.openFileChooser(uploadMsg)
            }

            fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
                mUploadMessage = uploadMsg
                pickFile()
            }
        })

        webView.loadUrl("file:///android_asset/demo.html")

        webView.registerHandler("submitFromWeb", object : BridgeHandler {
            override fun handler(data: String?, function: CallBackFunction) {
                Log.i(TAG, "handler = submitFromWeb, data from web = $data")
                function.onCallBack("submitFromWeb exe, response data 中文 from Java")
            }
        })

        val user = User()
        val location = Location()
        location.address = "SDU"
        user.location = location
        user.name = "大头鬼"

        webView.callHandler("functionInJs", Gson().toJson(user), object : CallBackFunction {
            override fun onCallBack(data: String?) {
                Log.d("a", data);
            }
        })

        webView.send("hello")

    }

    fun pickFile() {
        val chooserIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooserIntent.type = "image/*"
        startActivityForResult(chooserIntent, RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return
            }
            val result = if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
            mUploadMessage!!.onReceiveValue(result)
            mUploadMessage = null
        }
    }
}
