package com.effective.android.webview.jsbridge

import android.content.Context

import com.tencent.smtt.sdk.WebView

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object BridgeUtil {

    val YY_OVERRIDE_SCHEMA = "yy://"
    val YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/"            //格式为   yy://return/{function}/returncontent
    val YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/"
    val EMPTY_STR = ""
    val UNDERLINE_STR = "_"
    val SPLIT_MARK = "/"

    val CALLBACK_ID_FORMAT = "JAVA_CB_%s"
    val JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');"
    val JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();"
    val JAVASCRIPT_STR = "javascript:"

    // 例子 javascript:WebViewJavascriptBridge._fetchQueue(); --> _fetchQueue
    fun parseFunctionName(jsUrl: String): String {
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replace("\\(.*\\);".toRegex(), "")
    }

    // 获取到传递信息的body值
    // url = yy://return/_fetchQueue/[{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
    fun getDataFromReturnUrl(url: String): String? {
        if (url.startsWith(YY_FETCH_QUEUE)) {
            // return = [{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
            return url.replace(YY_FETCH_QUEUE, EMPTY_STR)
        }

        // temp = _fetchQueue/[{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
        val temp = url.replace(YY_RETURN_DATA, EMPTY_STR)
        val functionAndData = temp.split(SPLIT_MARK.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        if (functionAndData.size >= 2) {
            val sb = StringBuilder()
            for (i in 1 until functionAndData.size) {
                sb.append(functionAndData[i])
            }
            // return = [{"responseId":"JAVA_CB_2_3957","responseData":"Javascript Says Right back aka!"}]
            return sb.toString()
        }
        return null
    }

    // 获取到传递信息的方法
    // url = yy://return/_fetchQueue/[{"responseId":"JAVA_CB_1_360","responseData":"Javascript Says Right back aka!"}]
    fun getFunctionFromReturnUrl(url: String): String? {
        // temp = _fetchQueue/[{"responseId":"JAVA_CB_1_360","responseData":"Javascript Says Right back aka!"}]
        val temp = url.replace(YY_RETURN_DATA, EMPTY_STR)
        val functionAndData = temp.split(SPLIT_MARK.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return if (functionAndData.size >= 1) {
            // functionAndData[0] = _fetchQueue
            functionAndData[0]
        } else null
    }


    /**
     * js 文件将注入为第一个script引用
     * @param view WebView
     * @param url url
     */
    fun webViewLoadJs(view: WebView, url: String) {
        var js = "var newscript = document.createElement(\"script\");"
        js += "newscript.src=\"$url\";"
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);"
        view.loadUrl("javascript:$js")
    }

    /**
     * 这里只是加载lib包中assets中的 WebViewJavascriptBridge.js
     * @param view webview
     * @param path 路径
     */
    fun webViewLoadLocalJs(view: WebView, path: String) {
        val jsContent = assetFile2Str(view.context, path)
        view.loadUrl("javascript:" + jsContent!!)
    }

    /**
     * 解析assets文件夹里面的代码,去除注释,取可执行的代码
     * @param c context
     * @param urlStr 路径
     * @return 可执行代码
     */
    fun assetFile2Str(c: Context, urlStr: String): String? {
        var `in`: InputStream? = null
        try {
            `in` = c.assets.open(urlStr)
            val bufferedReader = BufferedReader(InputStreamReader(`in`!!))
            var line: String? = null
            val sb = StringBuilder()
            do {
                line = bufferedReader.readLine()
                if (line != null && !line.matches("^\\s*\\/\\/.*".toRegex())) { // 去除注释
                    sb.append(line)
                }
            } while (line != null)

            bufferedReader.close()
            `in`.close()

            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                }

            }
        }
        return null
    }
}
