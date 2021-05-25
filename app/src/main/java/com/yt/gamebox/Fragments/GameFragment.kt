package com.yt.gamebox.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.yt.gamebox.R
import com.yt.gamebox.WebViewActivity
import com.yt.gamebox.Adapters.DetailAdapter
import com.yt.gamebox.Utils.GlideUtil
import com.yt.gamebox.model.GameBean

class GameFragment(
    private val mActivity: Activity,
    private val mContext: Context = mActivity.applicationContext
) : Fragment() {
    private lateinit var gameList: ListView
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var pinballBG: ImageView
    private lateinit var pingusBG: ImageView
    private lateinit var expressBG: ImageView
    private lateinit var pinballIcon: ImageView
    private lateinit var pingusIcon: ImageView
    private lateinit var expressIcon: ImageView
    private lateinit var pinballName: TextView
    private lateinit var pingusName: TextView
    private lateinit var expressName: TextView
    private lateinit var pinballDownload: TextView
    private lateinit var pingusDownload: TextView
    private lateinit var expressDownload: TextView
    private lateinit var pinballLike: TextView
    private lateinit var pingusLike: TextView
    private lateinit var expressLike: TextView

    private lateinit var listSP: MutableList<Any>
    private lateinit var listGame: MutableList<Any>

    val gameBean0 = GameBean(
        id = 0,
        gameName = "砰砰弹力球",
        gameUrl = "http://www.yituogame.com/H5/WoDongBall-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.pinball_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.pinball_icon,
        gameDownloads = "230万",
        gameLikes = "711万"
    )
    val gameBean1 = GameBean(
        id = 1,
        gameName = "企鹅大战",
        gameUrl = "http://www.yituogame.com/H5/NanjiHelp-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.pingus_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.pingus_icon,
        gameDownloads = "20万",
        gameLikes = "17万"
    )
    val gameBean2 = GameBean(
        id = 2,
        gameName = "快递小哥冲冲冲",
        gameUrl = "http://www.yituogame.com/H5/KuaiDiBro-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.express_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.express_icon,
        gameDownloads = "30万",
        gameLikes = "19万"
    )
    val gameBean3 = GameBean(
        id = 3,
        gameName = "解救小猫",
        gameUrl = "http://www.yituogame.com/H5/Cat-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.cat_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.cat_icon,
        gameDownloads = "230万",
        gameLikes = "7万"
    )
    val gameBean4 = GameBean(
        id = 4,
        gameName = "充电机器人",
        gameUrl = "http://www.yituogame.com/H5/ChongDianRobot-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.robot_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.robot_icon,
        gameDownloads = "130万",
        gameLikes = "9万"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val contentView: View = inflater.inflate(R.layout.fragment_game, container, false)
        listSP = ArrayList()
        listSP.add(gameBean0)
        listSP.add(gameBean1)
        listSP.add(gameBean2)

        listGame = ArrayList()
        listGame.add(gameBean3)
        listGame.add(gameBean4)


        val bean0 = listSP.get(0) as GameBean
        val bean1 = listSP.get(1) as GameBean
        val bean2 = listSP.get(2) as GameBean
        pinballBG = contentView.findViewById(R.id.pinball_bg)
        if (bean0.gameBGUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, bean0.gameBGResID, pinballBG)
        else
            GlideUtil.displayImgByUri(mContext, bean0.gameBGUrl, pinballBG)
        pinballBG.setOnClickListener {
            gotoGamePage(0)
        }
        pingusBG = contentView.findViewById(R.id.pingus_bg)
        if (bean1.gameBGUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, bean1.gameBGResID, pingusBG)
        else
            GlideUtil.displayImgByUri(mContext, bean1.gameBGUrl, pingusBG)
        pingusBG.setOnClickListener {
            gotoGamePage(1)
        }
        expressBG = contentView.findViewById(R.id.express_bg)
        if (bean2.gameBGUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, bean2.gameBGResID, expressBG)
        else
            GlideUtil.displayImgByUri(mContext, bean2.gameBGUrl, expressBG)
        expressBG.setOnClickListener {
            gotoGamePage(2)
        }

        pinballIcon = contentView.findViewById(R.id.pinball_icon)
        if (bean0.gameIconUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, bean0.gameIconResID, pinballIcon)
        else
            GlideUtil.displayImgByUri(mContext, bean0.gameIconUrl, pinballIcon)
        pingusIcon = contentView.findViewById(R.id.pingus_icon)
        if (bean1.gameIconUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, bean1.gameIconResID, pingusIcon)
        else
            GlideUtil.displayImgByUri(mContext, bean1.gameIconUrl, pingusIcon)
        expressIcon = contentView.findViewById(R.id.express_icon)
        if (bean2.gameIconUrl.isEmpty())
            GlideUtil.displayImgByResId(mContext, bean2.gameIconResID, expressIcon)
        else
            GlideUtil.displayImgByUri(mContext, bean2.gameIconUrl, expressIcon)

        pinballName = contentView.findViewById(R.id.pinball_name)
        pinballName.setText(bean0.gameName)
        pingusName = contentView.findViewById(R.id.pingus_name)
        pingusName.setText(bean1.gameName)
        expressName = contentView.findViewById(R.id.express_name)
        expressName.setText(bean2.gameName)

        pinballLike = contentView.findViewById(R.id.pinball_like)
        pinballLike.setText(bean0.gameLikes)
        pingusLike = contentView.findViewById(R.id.pingus_like)
        pingusLike.setText(bean1.gameLikes)
        expressLike = contentView.findViewById(R.id.express_like)
        expressLike.setText(bean2.gameLikes)

        pinballDownload = contentView.findViewById(R.id.pinball_download)
        pinballDownload.setText(bean0.gameDownloads)
        pingusDownload = contentView.findViewById(R.id.pingus_download)
        pingusDownload.setText(bean1.gameDownloads)
        expressDownload = contentView.findViewById(R.id.express_download)
        expressDownload.setText(bean2.gameDownloads)

        gameList = contentView.findViewById(R.id.game_list)
        gameList.setOnScrollListener(scrollListener)
        detailAdapter = DetailAdapter(mActivity, mContext)

        detailAdapter.setContent(listGame)
        gameList.adapter = detailAdapter

        return contentView
    }


    fun gotoGamePage(position: Int) {
        val intent = Intent(mActivity, WebViewActivity::class.java)
        when (position) {
            0, 1, 2 -> {
                val gameBean = listSP.get(position) as GameBean
                intent.putExtra("title", gameBean.gameName)
                intent.putExtra("url", gameBean.gameUrl)
            }
        }
        startActivity(intent)
    }

    val scrollListener: AbsListView.OnScrollListener = object : AbsListView.OnScrollListener {
        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
            when (scrollState) {
                AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
//                    toggle()
                }
                AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
//                    toggle()
                }
                AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
//                    toggle()
                }
            }
        }

        override fun onScroll(
            view: AbsListView,
            firstVisibleItem: Int,
            visibleItemCount: Int,
            totalItemCount: Int
        ) {
        }
    }


    fun refreshList() {
        detailAdapter.refreshAdapter(listGame)
    }

    fun getGameList(): ListView {
        return gameList
    }
}