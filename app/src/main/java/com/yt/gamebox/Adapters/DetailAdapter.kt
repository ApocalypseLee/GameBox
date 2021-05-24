package com.yt.gamebox.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yt.gamebox.R
import com.yt.gamebox.WebViewActivity
import com.yt.gamebox.model.CustomCallback
import java.util.*
import kotlin.collections.ArrayList

class DetailAdapter(var activity: Activity, var context: Context) : BaseAdapter(), CustomCallback {
    companion object {
        val VIEW_TYPE_GAME = 1
        val VIEW_TYPE_WALLET = 2
        val VIEW_TYPE_COUNT = VIEW_TYPE_WALLET + 1
    }

    var contentView: MutableList<Int> = ArrayList()
    var inflater: LayoutInflater? = LayoutInflater.from(context)

    fun setContent(list: MutableList<Int>) {
        contentView.addAll(list)
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
                        view = inflater!!.inflate(R.layout.item_game_detail, parent, false)
                        bindViewHolder(view, type, position)
                    }
                }
                VIEW_TYPE_WALLET -> {
                    if (getItem(position) != null) {
                        view = inflater!!.inflate(R.layout.item_wallet_takeout, parent, false)
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
        return contentView.get(position)
    }

    override fun getViewTypeCount(): Int {
        return contentView.size
    }

    fun bindViewHolder(view: View, type: Int, position: Int) {
        when (type) {
            VIEW_TYPE_GAME -> initHolderGame(view, position)
            VIEW_TYPE_WALLET -> initHolderWallet(view, position)
        }
    }

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    private fun initHolderGame(view: View, position: Int) {
        val holderGame = ViewHolderGame()
        holderGame.gameTitle = view.findViewById(R.id.game_item_title)
        holderGame.gameLike = view.findViewById(R.id.game_item_like)
        holderGame.gamePower = view.findViewById(R.id.game_item_power)
        holderGame.gameBG = view.findViewById(R.id.game_item_bg)
        holderGame.gameIcon = view.findViewById(R.id.game_item_icon)
        holderGame.gameBG.setOnClickListener {
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
                holderGame.gameTitle.setText("解救小猫")
                holderGame.gameLike.setText("230万")
                holderGame.gamePower.setText("7万")
                holderGame.gameBG.background = context.getDrawable(R.drawable.cat_bg)
                holderGame.gameIcon.background = context.getDrawable(R.drawable.cat_icon)
            }
            1 -> {
                holderGame.gameTitle.setText("充电机器人")
                holderGame.gameLike.setText("130万")
                holderGame.gamePower.setText("9万")
                holderGame.gameBG.background = context.getDrawable(R.drawable.robot_bg)
                holderGame.gameIcon.background = context.getDrawable(R.drawable.robot_icon)
            }
        }
    }

    private fun initHolderWallet(view: View, position: Int) {
        when (position) {
            0 -> {
                val holderWallet = ViewHolderWallet()
                initWalletNewbie(holderWallet, view)
            }
        }

    }

    private fun initWalletNewbie(
        holderWallet: ViewHolderWallet,
        view: View
    ) {
        holderWallet.newbieContent = view.findViewById(R.id.newbie_content)
        holderWallet.newbieTakeOut = view.findViewById(R.id.newbie_takeoff)
        holderWallet.newbieTakeOut.setOnClickListener {

        }
        holderWallet.wallet1 = view.findViewById(R.id.wallet_lv1)
        holderWallet.wallet1.setOnClickListener {

        }
        holderWallet.cash1 = view.findViewById(R.id.cash_lv1)
        holderWallet.content1 = view.findViewById(R.id.cash_lv1_content)
        holderWallet.wallet2 = view.findViewById(R.id.wallet_lv2)
        holderWallet.wallet2.setOnClickListener {

        }
        holderWallet.cash2 = view.findViewById(R.id.cash_lv2)
        holderWallet.content2 = view.findViewById(R.id.cash_lv2_content)
        holderWallet.wallet3 = view.findViewById(R.id.wallet_lv3)
        holderWallet.wallet3.setOnClickListener {

        }
        holderWallet.cash3 = view.findViewById(R.id.cash_lv3)
        holderWallet.content3 = view.findViewById(R.id.cash_lv3_content)
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    fun refreshAdapter(contentView: MutableList<Int>) {
        this.contentView = contentView
        notifyDataSetChanged()
    }

    internal class ViewHolderGame {
        lateinit var gameTitle: TextView
        lateinit var gameLike: TextView
        lateinit var gamePower: TextView
        lateinit var gameBG: ImageView
        lateinit var gameIcon: ImageView
    }

    internal class ViewHolderWallet {
        lateinit var newbieContent: TextView
        lateinit var newbieTakeOut: ImageView
        lateinit var wallet1: LinearLayout
        lateinit var cash1: TextView
        lateinit var content1: TextView
        lateinit var wallet2: LinearLayout
        lateinit var cash2: TextView
        lateinit var content2: TextView
        lateinit var wallet3: LinearLayout
        lateinit var cash3: TextView
        lateinit var content3: TextView
    }


    override fun notice(datamap: Map<String, Objects>?) {
        refreshAdapter(contentView)
    }
}