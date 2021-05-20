package com.yt.gamebox.Widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import java.util.*


class SpinnerLoading(mContext: Context) : View(mContext) {
    private var mPaint: Paint? = null
    private var outRadius = 0
    private var innerRadius = 0
    private val ringWidth = 20
    private val center = IntArray(2)
    private var startIndex = -90f
    private var endIndex = 0f
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg);
            invalidate();
        }
    }

    init {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.color = Color.WHITE
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        center[0] = width / 2
        center[1] = height / 2
        outRadius = (if (width > height) height / 2 else width / 2) - ringWidth
        innerRadius = outRadius - ringWidth
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        mPaint!!.strokeWidth = ringWidth.toFloat()
        mPaint!!.setARGB(0, 0, 0, 0)
        canvas.drawCircle(
            center[0].toFloat(),
            center[1].toFloat(), innerRadius.toFloat(), mPaint!!
        )
        mPaint!!.setARGB(255, 0, 0, 255)
        canvas.drawCircle(
            center[0].toFloat(), center[1].toFloat(), outRadius.toFloat(),
            mPaint!!
        )
        mPaint!!.setARGB(255, 255, 255, 255)
        val rect = RectF(
            ringWidth.toFloat(),
            ringWidth.toFloat(),
            (ringWidth + 2 * outRadius).toFloat(),
            (ringWidth + 2 *
                    outRadius).toFloat()
        )
        canvas.drawArc(rect, startIndex, endIndex, false, mPaint!!)
        super.onDraw(canvas)
    }

    fun startLoading() {
        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                startIndex++
                if (startIndex < 0) {
                    endIndex++
                } else if (startIndex > 180) {
                    endIndex--
                    if (startIndex == 270f) {
                        startIndex = -90f
                        endIndex = 0f
                    }
                } else {
                    endIndex = 90f
                }
                mHandler.obtainMessage(1).sendToTarget()
            }
        }
        timer.schedule(timerTask, 100, 5)
    }
}