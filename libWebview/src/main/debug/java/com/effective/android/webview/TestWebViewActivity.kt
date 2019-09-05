package com.effective.android.webview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.effective.android.webview.bean.Request
import com.effective.android.webview.bean.Result
import com.effective.android.webview.interfaces.BridgeHandler
import com.effective.android.webview.interfaces.CallBackFunction
import com.google.gson.reflect.TypeToken
import com.tencent.smtt.sdk.WebView
import android.annotation.TargetApi
import android.os.Build
import androidx.annotation.RequiresApi


class TestWebViewActivity : Activity() {

    private val TAG = "TestWebViewActivity"


    private val JS_METHOD = "functionInJs"
    private val NATIVE_METHOD = "functionInNative"

    private var mValueCallback: com.tencent.smtt.sdk.ValueCallback<Uri>? = null
    private var mFilePathCallback: com.tencent.smtt.sdk.ValueCallback<Array<Uri>>? = null

    private val FILE_CHOOSER_RESULT_CODE = 0x01

    private fun getVersionCode(context: Context): String? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0)?.versionCode?.toString()
        } catch (e: Exception) {
            ""
        }
    }


    private fun setContent(content: String, nativeContent: TextView) {
        val currentContent = nativeContent.text.toString()
        nativeContent.text = currentContent + "\n" + content
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_test)
        val nativeContent = findViewById<View>(R.id.content) as TextView
        val userName = findViewById<View>(R.id.username) as EditText
        val password = findViewById<View>(R.id.password) as EditText
        val sendToWeb = findViewById<View>(R.id.send_to_web) as TextView
        val callWeb = findViewById<View>(R.id.call_web) as TextView
        val webView = findViewById<View>(R.id.webView) as X5JsWebView

        sendToWeb.setOnClickListener {
            val message = "Hello， I'm android!"
            val log = "android : $message"
            webView.send(message, object : CallBackFunction {
                override fun onCallBack(data: String?) {
                    if (!TextUtils.isEmpty(data)) {
                        val log = "web : $data"
                        Log.i(TAG, log)
                        setContent(log, nativeContent)
                    }
                }
            })
            Log.i(TAG, log)
            setContent(log, nativeContent)
        }

        webView.setDefaultHandler(object : BridgeHandler {
            override fun handler(data: String?, function: CallBackFunction) {
                if (!TextUtils.isEmpty(data)) {
                    val log = "web : $data"
                    Log.i(TAG, log)
                    setContent(log, nativeContent)
                    val reponse = "Hi, I'm android!"
                    val reponseLog = "android : $reponse"
                    function.onCallBack(reponse)
                    Log.i(TAG, reponseLog)
                    setContent(reponseLog, nativeContent)
                }
            }
        })

        callWeb.setOnClickListener {
            val userName = userName.editableText.toString()
            val password = password.editableText.toString()
            val userBean = UserBean(userName, password, "android")
            val platform = "android"
            var version = getVersionCode(this)
            if (TextUtils.isEmpty(version)) {
                version = ""
            }
            val request = Request(platform, version!!, userBean)
            var requestData = Utils.object2Json(request)
            if (TextUtils.isEmpty(requestData)) {
                requestData = ""
            }
            val log = "android request($JS_METHOD) : $requestData"
            Log.i(TAG, log)
            setContent(log, nativeContent)
            webView.callHandler(JS_METHOD, requestData, object : CallBackFunction {
                override fun onCallBack(data: String?) {
                    if (!TextUtils.isEmpty(data)) {
                        val log = "web reponse($JS_METHOD) : $data"
                        Log.i(TAG, log)
                        setContent(log, nativeContent)
                        var result = Utils.json2Obj<Result<String>>(data!!, object : TypeToken<Result<String>>() {}.type)
                    }
                }
            })
        }

        webView.registerHandler(NATIVE_METHOD, object : BridgeHandler {
            override fun handler(data: String?, function: CallBackFunction) {
                if (!TextUtils.isEmpty(data)) {
                    var request = Utils.json2Obj<Request<UserBean>>(data!!, object : TypeToken<Request<UserBean>>() {}.type)
                    val requestLog = "web request($NATIVE_METHOD) : $data"
                    Log.i(TAG, requestLog)
                    setContent(requestLog!!, nativeContent)
                    val json = Utils.object2Json(Result(200, "success", ""))
                    function.onCallBack(json)
                    val reponseLog = "android reponse($NATIVE_METHOD) : $json"
                    Log.i(TAG, reponseLog)
                    setContent(reponseLog, nativeContent)
                }
            }
        })

        webView.webChromeClient = object : X5WebChromeClient() {

            override fun openFileChooser(valueCallback: com.tencent.smtt.sdk.ValueCallback<Uri>) {
                super.openFileChooser(valueCallback)
                mValueCallback = valueCallback
                openImageChooserActivity()
            }

            override fun openFileChooser(valueCallback: com.tencent.smtt.sdk.ValueCallback<Uri>, acceptType: String) {
                super.openFileChooser(valueCallback, acceptType)
                mValueCallback = valueCallback
                openImageChooserActivity()
            }

            override fun openFileChooser(valueCallback: com.tencent.smtt.sdk.ValueCallback<Uri>, acceptType: String?, capture: String?) {
                super.openFileChooser(valueCallback, acceptType, capture)
                mValueCallback = valueCallback
                openImageChooserActivity()
            }

            @RequiresApi(21)
            override fun onShowFileChooser(webView: WebView?, filePathCallback: com.tencent.smtt.sdk.ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
                mFilePathCallback = filePathCallback
                openImageChooserActivity()
            }

        }
        webView.loadUrl("file:///android_asset/demo.html")
    }


    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i,
                "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mValueCallback && null == mFilePathCallback) return
            val result = if (data == null || resultCode != Activity.RESULT_OK) null else data.data
            if (mFilePathCallback != null) {
                onActivityResultAboveL(requestCode, resultCode, data)
            } else if (mValueCallback != null) {
                mValueCallback!!.onReceiveValue(result)
                mValueCallback = null
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mFilePathCallback == null)
            return
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {
                    results = Array(clipData.itemCount, { Uri.Builder().build() })
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null)
                    results = arrayOf(Uri.parse(dataString))
            }
        }
        mFilePathCallback!!.onReceiveValue(results)
        mFilePathCallback = null
    }

}
