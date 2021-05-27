package com.yt.gamebox.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yt.gamebox.Widgets.CustomViewHolder


abstract class BaseCustomAdapter<T>(
    val mContext: Context,
    var mLayoutId: Int,
    var itemData: List<T>,
    val mInflater: LayoutInflater = LayoutInflater.from(mContext),
) :
    RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder.get(mContext, parent, mLayoutId);
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        convert(holder, itemData.get(position));
    }

    override fun getItemCount(): Int {
        return itemData.size
    }

    abstract fun convert(holder: CustomViewHolder, data: T)
}