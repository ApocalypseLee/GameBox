package com.yt.gamebox.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yt.gamebox.Adapters.DetailAdapter
import com.yt.gamebox.R
import com.yt.gamebox.Utils.GlideUtil
import com.yt.gamebox.Utils.SystemProperties
import com.yt.gamebox.model.NewbieBean

class WalletFragment(
    private val mActivity: Activity,
    private var mContext: Context = mActivity.applicationContext
) : Fragment() {
    private lateinit var walletList: ListView
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var avatar: ImageView
    private lateinit var bindWX: ImageView
    private lateinit var userName: TextView
    private lateinit var userID: TextView
    private lateinit var totalCash: TextView
    private lateinit var canTakeCash: TextView

    private lateinit var dataList: MutableList<Any>

    val newbieBean = NewbieBean(
        id = 0,
        newbieCash = "0.3",
        walletCashLV1 = "25",
        walletCashLV2 = "30",
        walletCashLV3 = "50",
        walletCostLV1 = "25000000",
        walletCostLV2 = "30000000",
        walletCostLV3 = "50000000"
    )

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val contentView: View = inflater.inflate(R.layout.fragment_wallet, container, false)

        dataList = ArrayList()
        dataList.add(newbieBean)


        avatar = contentView.findViewById(R.id.avatar)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_avatar)
        GlideUtil.displayRoundImgByBitmap(mContext, bitmap, avatar, R.drawable.loading)
        bindWX = contentView.findViewById(R.id.bind_wx)
        bindWX.setOnClickListener {
            // TODO: 2021/5/24  wx login
        }
        userName = contentView.findViewById(R.id.user_name)
        userName.setText("游客" + SystemProperties.getUUID())

        userID = contentView.findViewById(R.id.user_id)
        userID.setText(SystemProperties.getUUID())

        totalCash = contentView.findViewById(R.id.cash_total)
//        totalCash.setText(SystemProperties.getUUID())
        canTakeCash = contentView.findViewById(R.id.cash_take)
//        canTakeCash.setText(SystemProperties.getUUID())

        walletList = contentView.findViewById(R.id.wallet_list)
        walletList.setOnScrollListener(scrollListener)
        detailAdapter = DetailAdapter(mActivity, mContext)
        detailAdapter.setContent(dataList)
        walletList.adapter = detailAdapter


        return contentView
    }

    val scrollListener: AbsListView.OnScrollListener = object : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            when (scrollState) {
                AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                }
                AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                }
                AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                }
            }
        }

        override fun onScroll(
            view: AbsListView?,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {
        }
    }
}