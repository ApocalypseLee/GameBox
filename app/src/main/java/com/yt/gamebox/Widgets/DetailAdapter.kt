package com.yt.gamebox.Widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.yt.gamebox.R
import com.yt.gamebox.WebViewActivity
import com.yt.gamebox.model.CustomCallback
import java.util.*
import kotlin.collections.ArrayList

class DetailAdapter(var activity: Activity, var context: Context) : BaseAdapter(), CustomCallback {
    val VIEW_TYPE_GAME = 1
    val VIEW_TYPE_COUNT = VIEW_TYPE_GAME + 1

    var position = 0
    var viewType = VIEW_TYPE_GAME
    var contentView: MutableList<Int> = ArrayList()
    var inflater: LayoutInflater? = LayoutInflater.from(context)

    fun setContent() {
        contentView.add(VIEW_TYPE_GAME)
        contentView.add(VIEW_TYPE_GAME)
    }

    override fun getCount(): Int {
        return contentView.size
    }

    override fun getItem(position: Int): Int? {
        return if (contentView.isEmpty() || position < 0 || position > contentView.size) null else contentView[position]
    }

    override fun getItemId(position: Int): Long {
        return if (contentView.isEmpty() || position < 0 || position > contentView.size) -1 else contentView[position].toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = null
        val type = getItemViewType(position)
        if (convertView == null) {
            when (type) {
                VIEW_TYPE_GAME -> {
                    if (getItem(position) != null) {
                        view = inflater!!.inflate(R.layout.game_layout, parent, false)
                        bindViewHolder(view, type, position)
                    }
                }
                else -> {
                    throw IllegalStateException("Unexpected value: $type")
                }
            }
        } else {
            view = convertView
            bindViewHolder(view, type, position)
        }
        return view!!
    }

    override fun getItemViewType(position: Int): Int {
//        if (position == 0) {
//            return VIEW_TYPE_HEAD
//        } else if (position % 2 == 0)
//            return VIEW_TYPE_ACC
//        else
//            return VIEW_TYPE_DETAIL
        return VIEW_TYPE_GAME
    }

    override fun getViewTypeCount(): Int {
        return contentView.size
    }

    fun bindViewHolder(view: View, type: Int, position: Int) {
        when (type) {
            VIEW_TYPE_GAME -> initHolderGame(view, position)
        }
    }

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    private fun initHolderGame(view: View, position: Int) {
        val holderGame = ViewHolderGame()
        holderGame.gameTitle = view.findViewById(R.id.game_item_title) as TextView
        holderGame.gameLike = view.findViewById(R.id.game_item_like) as TextView
        holderGame.gamePower = view.findViewById(R.id.game_item_power) as TextView
        holderGame.gameBG = view.findViewById(R.id.game_item_bg) as ImageView
        holderGame.gameIcon = view.findViewById(R.id.game_item_icon) as ImageView
        holderGame.gameBG!!.setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            when (position) {
                0 -> {
                    intent.putExtra("title", "解救小猫")
                    intent.putExtra("url", "")
                }
                1 -> {
                    intent.putExtra("title", "充电机器人")
                    intent.putExtra("url", "")
                }
            }
            activity.startActivity(intent)
        }

        when (position) {
            0 -> {
                holderGame.gameTitle!!.setText("解救小猫")
                holderGame.gameLike!!.setText("230万")
                holderGame.gamePower!!.setText("7万")
                holderGame.gameBG!!.background = context.getDrawable(R.drawable.cat_bg)
                holderGame.gameIcon!!.background = context.getDrawable(R.drawable.cat_icon)
            }
            1 -> {
                holderGame.gameTitle!!.setText("充电机器人")
                holderGame.gameLike!!.setText("130万")
                holderGame.gamePower!!.setText("9万")
                holderGame.gameBG!!.background = context.getDrawable(R.drawable.robot_bg)
                holderGame.gameIcon!!.background = context.getDrawable(R.drawable.robot_icon)
            }
        }
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    fun refreshAdapter(contentView: MutableList<Int>) {
        this.contentView = contentView
        notifyDataSetChanged()
    }

    internal class ViewHolderGame {
        var gameTitle: TextView? = null
        var gameLike: TextView? = null
        var gamePower: TextView? = null
        var gameBG: ImageView? = null
        var gameIcon: ImageView? = null
    }

    override fun notice(datamap: Map<String, Objects>?) {
        val contentView: MutableList<Int> = ArrayList()
        contentView.add(VIEW_TYPE_GAME)
        contentView.add(VIEW_TYPE_GAME)
        refreshAdapter(contentView)
    }
}