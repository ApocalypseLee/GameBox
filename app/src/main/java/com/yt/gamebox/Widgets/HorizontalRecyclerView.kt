package com.yt.gamebox.Widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView


class HorizontalRecyclerView(context: Context, attributeSet: AttributeSet) :
    RecyclerView(context, attributeSet) {

    private var x1 = 0f
    private var y1 = 0f
    private var mViewParent: ViewParent? = null


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        //解决recyclerView和viewPager的滑动影响
        //当滑动recyclerView时，告知父控件不要拦截事件，交给子view处理
        get(false)
        when (event.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                //当手指按下的时候
                x1 = event.getX()
                y1 = event.getY()
            }
            MotionEvent.ACTION_MOVE -> {
                //当手指移动的时候
                val x2: Float = event.getX()
                val y2: Float = event.getY()
                val offsetX = Math.abs(x2 - x1)
                val offsetY = Math.abs(y2 - y1)
                if (offsetX >= offsetY) {
                    get(true) //手指左移
                } else {
                    get(false)
                }
            }
            MotionEvent.ACTION_UP -> {
                run {
                    y1 = 0f
                    x1 = y1
                }
                get(false)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    //使用迭代 直至找到parent是NoScrollViewPager为止
    //效率有些低 偏low 莫见怪
    private operator fun get(isEnable: Boolean) {
        mViewParent = if (mViewParent == null) parent else mViewParent!!.parent
        if (mViewParent is NoScrollViewPager) {
            //true 禁止ViewPager滑动，自动交给recyclerview去滑动
            //false 交给ViewPager滑动
            val viewPager: NoScrollViewPager = mViewParent as NoScrollViewPager
            viewPager.setNoScroll(isEnable)
        } else {
            get(isEnable)
        }
    }
}