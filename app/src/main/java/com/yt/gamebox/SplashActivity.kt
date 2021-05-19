package com.yt.gamebox

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private val TAG = SplashActivity::class.java.simpleName
    var clicked = false
    var paused: Boolean = false
    private val AD_TIME_OUT = 3500L

    private var mStartedCount = 0
    private val handler = Handler()
    private val countDownTimer: CountDownTimer =
        object : CountDownTimer(AD_TIME_OUT, 500) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                toNextActivity()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
        countDownTimer.start()
    }

    override fun onStart() {
        super.onStart()
        mStartedCount++
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun onResume() {
        super.onResume()
        paused = false
        if (clicked) {
            toNextActivity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }


    /**
     * 开屏页一定要禁止用户对返回按钮的控制，
     * 否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    /**
     * 过滤掉 冷启动界面 从后台切回到前台时的计数统计
     *
     * @return
     */
    fun needStatistics(isOnStartCall: Boolean): Boolean {
        return if (isOnStartCall) mStartedCount < 1 else mStartedCount < 2
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当广告被点击，可能会跳转其他页面，此时开发者还不能打开自己的App主页。当从其他页面返回以后， 才可以跳转到开发者自己的App主页；
     */
    private operator fun next() {
        if (!clicked) {
            toNextActivity()
        }
    }

    private fun toNextActivity() {
        val intent = Intent(this@SplashActivity, FullscreenActivity::class.java)
        startActivity(intent)
        //        mSplashContainer.removeAllViews();
        finish()
    }
}