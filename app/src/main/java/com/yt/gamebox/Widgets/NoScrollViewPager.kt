package com.yt.gamebox.Widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager(context: Context, attributeSet: AttributeSet) :
    ViewPager(context, attributeSet) {
    // 是否禁止 viewpager 左右滑动
    private var noScroll = false

    fun setNoScroll(noScroll: Boolean) {
        this.noScroll = noScroll
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (noScroll) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (noScroll) {
            return false;
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }
}