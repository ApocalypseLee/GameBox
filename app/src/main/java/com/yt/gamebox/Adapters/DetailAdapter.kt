package com.yt.gamebox.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yt.gamebox.R
import com.yt.gamebox.Utils.GlideUtil
import com.yt.gamebox.WebViewActivity
import com.yt.gamebox.Widgets.CustomViewHolder
import com.yt.gamebox.model.CustomCallback
import com.yt.gamebox.model.GameBean
import com.yt.gamebox.model.NewbieBean

class DetailAdapter<T>(
    var activity: Activity,
    context: Context,
    layoutID: Int,
    itemData: List<T>
) : BaseCustomAdapter<T>(context, layoutID, itemData), CustomCallback {

    companion object {
        val VIEW_TYPE_GAME = 1
        val VIEW_TYPE_WALLET = 2
        val VIEW_TYPE_COUNT = VIEW_TYPE_WALLET + 1
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

    override fun convert(holder: CustomViewHolder, data: T) {
        if (data is GameBean) {
            initHolderGame(holder, data)
        } else if (data is NewbieBean) {
            initHolderWallet(holder, data)
        }
    }

    private fun initHolderGame(holder: CustomViewHolder, data: GameBean) {
        val gameIcon = holder.getView<ImageView>(R.id.game_item_icon)
        if (data.gameIconUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, data.gameIconResID, gameIcon)
        else
            GlideUtil.displayImgByUri(mContext, data.gameIconUrl, gameIcon)

        val gameBG = holder.getView<ImageView>(R.id.game_item_bg)
        if (data.gameBGUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, data.gameBGResID, gameBG)
        else
            GlideUtil.displayImgByUri(mContext, data.gameBGUrl, gameBG)

        val gameTitle = holder.getView<TextView>(R.id.game_item_title)
        gameTitle.text = data.gameName

        val gameLike = holder.getView<TextView>(R.id.game_item_like)
        gameLike.text = data.gameLikes

        val gamePower = holder.getView<TextView>(R.id.game_item_power)
        gamePower.text = data.gameDownloads

        gameBG.setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("title", data.gameName)
            intent.putExtra("url", data.gameUrl)
            activity.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initHolderWallet(holder: CustomViewHolder, data: NewbieBean) {
        val newbieContent = holder.getView<TextView>(R.id.newbie_content)
        newbieContent.setText(data.newbieCash + "元")
        val newbieTakeOut = holder.getView<ImageView>(R.id.newbie_takeoff)
        newbieTakeOut.setOnClickListener {

        }

        val wallet1 = holder.getView<LinearLayout>(R.id.wallet_lv1)
        wallet1.setOnClickListener {

        }
        val cash1 = holder.getView<TextView>(R.id.cash_lv1)
        cash1.setText(data.walletCashLV1 + "元")
        val content1 = holder.getView<TextView>(R.id.cash_lv1_content)
        content1.setText(data.walletCostLV1 + "元")

        val wallet2 = holder.getView<LinearLayout>(R.id.wallet_lv2)
        wallet2.setOnClickListener {

        }
        val cash2 = holder.getView<TextView>(R.id.cash_lv2)
        cash2.setText(data.walletCashLV2 + "元")
        val content2 = holder.getView<TextView>(R.id.cash_lv2_content)
        content2.setText(data.walletCostLV2 + "元")

        val wallet3 = holder.getView<LinearLayout>(R.id.wallet_lv3)
        wallet3.setOnClickListener {

        }
        val cash3 = holder.getView<TextView>(R.id.cash_lv3)
        cash3.setText(data.walletCashLV3 + "元")
        val content3 = holder.getView<TextView>(R.id.cash_lv3_content)
        content3.setText(data.walletCostLV3 + "元")
    }


    fun refreshAdapter(data: List<T>) {
        itemData = data
        notifyDataSetChanged()
    }


    override fun notice(datamap: Map<String, Any>) {
        refreshAdapter(itemData)
    }
}