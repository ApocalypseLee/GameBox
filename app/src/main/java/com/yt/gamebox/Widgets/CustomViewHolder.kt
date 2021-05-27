package com.yt.gamebox.Widgets

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


@Suppress("UNCHECKED_CAST")
class CustomViewHolder(
    private val mConvertView: View,
    val parent: ViewGroup,
    val context: Context
) : RecyclerView.ViewHolder(mConvertView) {

    private var mViews: HashMap<Int, View> = HashMap()

    companion object {
        fun get(context: Context, parent: ViewGroup, layoutId: Int): CustomViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return CustomViewHolder(itemView, parent, context)
        }
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    @SuppressLint("NewApi")
    fun <T : View?> getView(viewId: Int): T {
        if (mViews.isEmpty() || !mViews.contains(viewId)) {
            val view: View = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        val view: View = mViews.get(viewId)!!
        return view as T
    }
}