package com.yt.gamebox

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.PersistableBundle
import android.view.KeyEvent
import android.view.Window

class SplashActivity : Activity() {

    private val AD_TIME_OUT = 3500L

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

    private fun toNextActivity() {
        val intent = Intent(this@SplashActivity, FullscreenActivity::class.java)
        startActivity(intent)
        finish()
    }
}