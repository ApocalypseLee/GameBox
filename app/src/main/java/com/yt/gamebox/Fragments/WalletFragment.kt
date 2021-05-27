package com.yt.gamebox.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yt.gamebox.Adapters.DetailAdapter
import com.yt.gamebox.R
import com.yt.gamebox.Utils.GlideUtil
import com.yt.gamebox.Utils.SystemProperties
import com.yt.gamebox.Widgets.HorizontalRecyclerView
import com.yt.gamebox.model.NewbieBean
import com.yt.gamebox.model.UserBean

class WalletFragment(
    private val mActivity: Activity,
    private var mContext: Context = mActivity.applicationContext
) : Fragment() {
    private lateinit var walletList: HorizontalRecyclerView
    private lateinit var detailAdapter: DetailAdapter<NewbieBean>
    private lateinit var avatar: ImageView
    private lateinit var bindWX: ImageView
    private lateinit var userName: TextView
    private lateinit var userID: TextView
    private lateinit var totalCash: TextView
    private lateinit var canTakeCash: TextView

    private lateinit var dataList: MutableList<NewbieBean>

    private lateinit var userBean: UserBean
    private lateinit var newbieBean: NewbieBean


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val contentView: View = inflater.inflate(R.layout.fragment_wallet, container, false)

        userBean = UserBean(
            id = 0,
            userID = "",
            userName = "",
            userAvatar = "",
            binded = false,
            cashTotal = "545000055444",
            cashTakeout = "685.74",
            newbieBean = NewbieBean(
                id = 0,
                newbieCash = "0.3",
                walletCashLV1 = "25",
                walletCashLV2 = "30",
                walletCashLV3 = "50",
                walletCostLV1 = "25000000",
                walletCostLV2 = "30000000",
                walletCostLV3 = "50000000"
            )
        )
        newbieBean = userBean.newbieBean

        dataList = ArrayList()
        dataList.add(newbieBean)


        avatar = contentView.findViewById(R.id.avatar)
        if (userBean.userAvatar.isEmpty()) {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_avatar)
            GlideUtil.displayRoundImgByBitmap(mContext, bitmap, avatar, R.drawable.loading)
        } else {
            GlideUtil.displayRoundImgByPath(
                mContext,
                userBean.userAvatar,
                avatar,
                R.drawable.default_avatar
            )
        }
        bindWX = contentView.findViewById(R.id.bind_wx)
        if (userBean.binded)
            bindWX.visibility = View.GONE
        bindWX.setOnClickListener {
            // TODO: 2021/5/24  wx login
        }
        userName = contentView.findViewById(R.id.user_name)
        if (userBean.userName.isEmpty())
            userName.setText("游客" + SystemProperties.getUUID())
        else
            userName.setText(userBean.userName)

        userID = contentView.findViewById(R.id.user_id)
        if (userBean.userID.isEmpty())
            userID.setText(SystemProperties.getUUID())
        else
            userID.setText(userBean.userID)

        totalCash = contentView.findViewById(R.id.cash_total)
        totalCash.setText(userBean.cashTotal)
        canTakeCash = contentView.findViewById(R.id.cash_take)
        canTakeCash.setText(userBean.cashTakeout)

        walletList = contentView.findViewById(R.id.wallet_list)
        walletList.layoutManager = LinearLayoutManager(mContext)
        detailAdapter = DetailAdapter(mActivity, mContext, R.layout.item_wallet_takeout, dataList)
        walletList.adapter = detailAdapter


        return contentView
    }
}