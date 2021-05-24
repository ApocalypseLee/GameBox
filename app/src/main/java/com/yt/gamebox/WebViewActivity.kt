package com.yt.gamebox

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var URL: String
    private val hideHandler = Handler()

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        webView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        webView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
    * Schedules a call to hide() in [delayMillis], canceling any
    * previously scheduled calls.
    */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.active_webview)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true

        webView = findViewById(R.id.game_web)
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
        URL = String()
        val url = intent.getStringExtra("url")
        URL = if (!TextUtils.isEmpty(url)) ({
            url
        }).toString()
        else {
            Constants.defaultGameUrl
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

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onResume() {
        super.onResume()
        delayedHide(100)
    }

    override fun onRestart() {
        super.onRestart()
        delayedHide(100)
    }

    override fun onBackPressed() {
        webView.goBack()
        webView.clearCache(true)
        val intent = Intent(this, FullscreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}