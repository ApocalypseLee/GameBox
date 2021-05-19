package com.yt.gamebox

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.webkit.*
import android.widget.ImageButton
import android.widget.TextView

class WebViewActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var webViewGobackBtn: ImageButton
    private lateinit var webViewCloseBtn: ImageButton
    private lateinit var title: TextView
    private lateinit var URL: String
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.active_webview)
        webViewGobackBtn = findViewById(R.id.gobackWebView)
        webViewCloseBtn = findViewById(R.id.closeWebView)
        title = findViewById(R.id.game_title)
        webViewGobackBtn.setOnClickListener(goback)
        webViewCloseBtn.setOnClickListener(goback)
        webView = findViewById(R.id.tuia_xuanfu)
        val webSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.allowFileAccess = true
        webSettings.domStorageEnabled = true
        webSettings.setAppCacheEnabled(true)
        webSettings.domStorageEnabled = true
        webSettings.supportMultipleWindows()
        webSettings.allowContentAccess = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.savePassword = true
        webSettings.saveFormData = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.builtInZoomControls = true
        val intent = intent ?: return
        val title = intent.getStringExtra("title")
        if (!TextUtils.isEmpty(title)) {
            this.title.setText(title)
        }
        URL = String()
        val url = intent.getStringExtra("url")
        URL = if (!TextUtils.isEmpty(url)) ({
            url
        }).toString()
        else {
            "http://www.yituogame.com/"
        }
        webView.loadUrl(URL)
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//                return super.shouldOverrideUrlLoading(view,url);
                webView.setWebChromeClient(WebChromeClient())
                view.loadUrl(url)
                return true
            }
        })
        class MyDownLoad : DownloadListener {
            override fun onDownloadStart(
                url: String, userAgent: String,
                contentDisposition: String, mimetype: String, contentLength: Long
            ) {
                if (url.endsWith(".apk")) {
                    /**
                     * 通过系统下载apk
                     */
                    val uri = Uri.parse(url)
                    val intent1 = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent1)
                }
            }
        }
        webView.setDownloadListener(MyDownLoad())
    }

    private val goback = View.OnClickListener { v ->
        if (v.id == R.id.gobackWebView) {
            webView.goBack()
        } else if (v.id == R.id.closeWebView) {
            webView.clearCache(true)
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}