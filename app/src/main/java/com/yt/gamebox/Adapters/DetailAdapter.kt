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
import com.yt.gamebox.Utils.GlideUtil
import com.yt.gamebox.WebViewActivity
import com.yt.gamebox.model.CustomCallback
import com.yt.gamebox.model.GameBean
import com.yt.gamebox.model.NewbieBean
import java.util.*
import kotlin.collections.ArrayList

class DetailAdapter(var activity: Activity, var context: Context) : BaseAdapter(), CustomCallback {
    companion object {
        val VIEW_TYPE_GAME = 1
        val VIEW_TYPE_WALLET = 2
        val VIEW_TYPE_COUNT = VIEW_TYPE_WALLET + 1
    }

    var itemData: MutableList<Any> = ArrayList()
    var inflater: LayoutInflater? = LayoutInflater.from(context)

    fun setContent(list: MutableList<Any>) {
        itemData.addAll(list)
    }

    override fun getCount(): Int {
        return itemData.size
    }

    override fun getItem(position: Int): Any? {
        return if (itemData.isEmpty() || position < 0 || position > itemData.size) null else itemData[position]
    }

    override fun getItemId(position: Int): Long {
        if (itemData[position] is GameBean) {
            val data: GameBean = itemData[position] as GameBean
            return data.id
        } else if (itemData[position] is NewbieBean) {
            val data: NewbieBean = itemData[position] as NewbieBean
            return data.id
        }
        return if (itemData.isEmpty() || position < 0 || position > itemData.size) -1 else itemData[position].hashCode()
            .toLong()
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
        if (itemData[position] is GameBean) {
            val data: GameBean = itemData[position] as GameBean
            return data.type
        } else if (itemData[position] is NewbieBean) {
            val data: NewbieBean = itemData[position] as NewbieBean
            return data.type
        }
        return -1
    }

    override fun getViewTypeCount(): Int {
        return itemData.size
    }

    fun bindViewHolder(view: View, type: Int, position: Int) {
        when (type) {
            VIEW_TYPE_GAME -> initHolderGame(view, position)
            VIEW_TYPE_WALLET -> initHolderWallet(view, position)
        }
    }

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    private fun initHolderGame(view: View, position: Int) {
        val data: GameBean = itemData[position] as GameBean
        val holderGame = ViewHolderGame()

        holderGame.gameTitle = view.findViewById(R.id.game_item_title)
        holderGame.gameTitle.setText(data.gameName)

        holderGame.gameLike = view.findViewById(R.id.game_item_like)
        holderGame.gameLike.setText(data.gameLikes)

        holderGame.gamePower = view.findViewById(R.id.game_item_power)
        holderGame.gamePower.setText(data.gameDownloads)

        holderGame.gameIcon = view.findViewById(R.id.game_item_icon)
        if (data.gameIconUrl.isEmpty())
            GlideUtil.displayImgByResId(context, data.gameIconResID, holderGame.gameIcon)
        else
            GlideUtil.displayImgByUri(context, data.gameIconUrl, holderGame.gameIcon)

        holderGame.gameBG = view.findViewById(R.id.game_item_bg)
        if (data.gameBGUrl.isEmpty())
            GlideUtil.displayImgByResId(context, data.gameBGResID, holderGame.gameBG)
        else
            GlideUtil.displayImgByUri(context, data.gameBGUrl, holderGame.gameBG)
        holderGame.gameBG.setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("title", data.gameName)
            intent.putExtra("url", data.gameUrl)
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

    @SuppressLint("SetTextI18n")
    private fun initHolderWallet(view: View, position: Int) {
        val data: NewbieBean = itemData[position] as NewbieBean
        val holderWallet = ViewHolderWallet()
        holderWallet.newbieContent = view.findViewById(R.id.newbie_content)
        holderWallet.newbieContent.setText(data.newbieCash + "元")
        holderWallet.newbieTakeOut = view.findViewById(R.id.newbie_takeoff)
        holderWallet.newbieTakeOut.setOnClickListener {

        }

        holderWallet.wallet1 = view.findViewById(R.id.wallet_lv1)
        holderWallet.wallet1.setOnClickListener {

        }
        holderWallet.cash1 = view.findViewById(R.id.cash_lv1)
        holderWallet.cash1.setText(data.walletCashLV1 + "元")
        holderWallet.content1 = view.findViewById(R.id.cash_lv1_content)
        holderWallet.content1.setText(data.walletCostLV1 + "元")

        holderWallet.wallet2 = view.findViewById(R.id.wallet_lv2)
        holderWallet.wallet2.setOnClickListener {

        }
        holderWallet.cash2 = view.findViewById(R.id.cash_lv2)
        holderWallet.cash2.setText(data.walletCashLV2 + "元")
        holderWallet.content2 = view.findViewById(R.id.cash_lv2_content)
        holderWallet.content2.setText(data.walletCostLV2 + "元")

        holderWallet.wallet3 = view.findViewById(R.id.wallet_lv3)
        holderWallet.wallet3.setOnClickListener {

        }
        holderWallet.cash3 = view.findViewById(R.id.cash_lv3)
        holderWallet.cash3.setText(data.walletCashLV3 + "元")
        holderWallet.content3 = view.findViewById(R.id.cash_lv3_content)
        holderWallet.content3.setText(data.walletCostLV3 + "元")
    }


    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    fun refreshAdapter(contentView: MutableList<Any>) {
        this.itemData = contentView
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


    override fun notice(datamap: Map<String, Any>) {
        refreshAdapter(itemData)
    }
}