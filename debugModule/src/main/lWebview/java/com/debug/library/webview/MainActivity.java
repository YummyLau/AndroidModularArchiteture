package com.debug.library.webview;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.debug.R;
import com.effective.android.base.activity.BaseActivity;
import com.effective.android.webview.Utils;
import com.effective.android.webview.X5JsWebView;
import com.effective.android.webview.X5WebChromeClient;
import com.effective.android.webview.interfaces.BridgeHandler;
import com.effective.android.webview.interfaces.CallBackFunction;
import com.effective.android.webview.jsbridge.DefaultHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


public class MainActivity extends BaseActivity {

    private X5JsWebView mWebView;
    private Button mCallJsBtn;
    private Toolbar mToolbar;
    private ProgressView mProgressView;

    private SearchUrlDialog mSearchUrlDialog;
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mHighVersionValueCallback;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String EXAMPLE_URL = "file:///android_asset/demo.html";
    private static final int CHOOSE_FILE_CODE = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_search: {
                        if (mSearchUrlDialog == null) {
                            mSearchUrlDialog = new SearchUrlDialog(MainActivity.this);
                        }
                        mSearchUrlDialog.setUrlAction(new SearchUrlDialog.UrlAction() {
                            @Override
                            public void onAction(String url) {
                                mWebView.loadUrl(url);
                                mSearchUrlDialog.dismiss();
                            }
                        });
                        mSearchUrlDialog.show();
                    }
                }
                return true;
            }
        });

        mProgressView = findViewById(R.id.progress_view);
        mProgressView.setColor(Color.YELLOW);


        mWebView = findViewById(R.id.webview);
        mWebView.setDefaultHandler(new DefaultHandler());
        mWebView.setWebChromeClient(new X5WebChromeClient() {

            @Override
            public void onProgressChanged(WebView webView, int i) {
                if (i == 100) {
                    mProgressView.setVisibility(View.GONE);
                } else {
                    mProgressView.setVisibility(View.VISIBLE);
                    mProgressView.setProgress(i);
                }
                super.onProgressChanged(webView, i);
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                mToolbar.setTitle(s);
            }

            /**
             * 5.0以上回调
             * @param webView
             * @param valueCallback
             * @param fileChooserParams
             * @return
             */
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                mHighVersionValueCallback = valueCallback;
                pickFile();
                return true;
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                mValueCallback = valueCallback;
                pickFile();
            }

        });
        mWebView.loadUrl(EXAMPLE_URL);
        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }
        });

        //java 调用 js 方法
        mCallJsBtn = findViewById(R.id.call_js);
        mCallJsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.callHandler("functionInJs", "data from java", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i(TAG, "response data from js " + data);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean result = Utils.hookKeyCode(mWebView, keyCode);
        if (!result) {
            return super.onKeyDown(keyCode, event);
        }
        return result;
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, CHOOSE_FILE_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CHOOSE_FILE_CODE) {
            if (mValueCallback == null && mHighVersionValueCallback == null) {
                return;
            }
            if (intent == null || requestCode != RESULT_OK) {
                mValueCallback = null;
                mHighVersionValueCallback = null;
                return;
            }

            if (mHighVersionValueCallback != null) {
                handleHighVersionFileChoose(intent);
            } else {
                mValueCallback.onReceiveValue(intent.getData());
                mValueCallback = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Utils.destroyWebView(mWebView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleHighVersionFileChoose(Intent intent) {
        Uri[] results = null;
        String dataString = intent.getDataString();
        ClipData clipData = intent.getClipData();
        if (clipData != null) {
            results = new Uri[clipData.getItemCount()];
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);
                results[i] = item.getUri();
            }
        }
        if (dataString != null)
            results = new Uri[]{Uri.parse(dataString)
            };
        if (results != null) {
            mHighVersionValueCallback.onReceiveValue(results);
        }
        mHighVersionValueCallback = null;
    }
}
