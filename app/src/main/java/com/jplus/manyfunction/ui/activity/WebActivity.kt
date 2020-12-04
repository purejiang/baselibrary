package com.jplus.manyfunction.ui.activity

import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.jplus.manyfunction.R
import kotlinx.android.synthetic.main.activity_web.*


class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val map = mutableMapOf<String, String>();
        //声明WebSettings子类

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        wbv_test.settings.javaScriptEnabled = true
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
//        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        s.setUseWideViewPort(true);
//        s.setJavaScriptEnabled(true);
//        s.setCacheMode(WebSettings.LOAD_DEFAULT);
//        s.setSupportZoom(false);
//        s.setJavaScriptCanOpenWindowsAutomatically(true);
//        lnWeb.addJavascriptInterface(this, WebViewJS.JS_OBJECT);
//        lnWeb.requestFocus();
//        lnWeb.setWebChromeClient(new LnWebChromeClient());
//        lnWeb.setWebViewClient(new LnWebViewClient());
//        lnWeb.setDrawingCacheEnabled(false);
        //支持插件
//        wbv_test.settings.setPluginsEnabled(true)

        //设置自适应屏幕，两者合用
        wbv_test.settings.useWideViewPort = true //将图片调整到适合webview的大小

        wbv_test.settings.useWideViewPort = true //将图片调整到适合webview的大小

        //缩放操作
        wbv_test.settings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。

        wbv_test.settings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放

        wbv_test.settings.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        wbv_test.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存

        wbv_test.settings.allowFileAccess = true //设置可以访问文件

        wbv_test.settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口

        wbv_test.settings.loadsImagesAutomatically = true //支持自动加载图片

        wbv_test.settings.defaultTextEncodingName = "utf-8" //设置编码格式

        wbv_test.loadUrl("https://www.baidu.com")
        wbv_test.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed() //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        }

        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wbv_test.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        wbv_test.webViewClient = object :WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                Log.d("pipa", "url:${request.url}")
                return super.shouldInterceptRequest(view, request);
            }
        }

            wbv_test.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }

        }
}
