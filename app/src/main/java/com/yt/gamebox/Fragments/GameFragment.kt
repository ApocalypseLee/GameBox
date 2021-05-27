package com.yt.gamebox.Fragments

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Shader.TileMode
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.yt.gamebox.Adapters.DetailAdapter
import com.yt.gamebox.R
import com.yt.gamebox.Utils.GlideUtil
import com.yt.gamebox.Widgets.HorizontalRecyclerView
import com.yt.gamebox.model.GameBean
import com.yt.gamebox.model.GamePageBean


class GameFragment(
    private val mActivity: Activity,
    private val mContext: Context = mActivity.applicationContext
) : Fragment() {
    private lateinit var gameTitle: ImageView
    private lateinit var gameList: HorizontalRecyclerView
    private lateinit var detailAdapter: DetailAdapter<GameBean>
    private lateinit var listGame: MutableList<GameBean>
    private lateinit var gamePageBean: GamePageBean

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
        gameName = "快递小哥冲鸭",
        gameUrl = "http://www.yituogame.com/H5/KuaiDiBro-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.express_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.express_icon,
        gameDownloads = "30万",
        gameLikes = "19万"
    )
    val gameBean2 = GameBean(
        id = 2,
        gameName = "解救小猫",
        gameUrl = "http://www.yituogame.com/H5/Cat-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.cat_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.cat_icon,
        gameDownloads = "230万",
        gameLikes = "7万"
    )
    val gameBean3 = GameBean(
        id = 3,
        gameName = "解救小鹅鹅",
        gameUrl = "http://www.yituogame.com/H5/NanjiHelp-web-mobile",
        gameBGUrl = "",
        gameBGResID = R.drawable.pingus_bg,
        gameIconUrl = "",
        gameIconResID = R.drawable.pingus_icon,
        gameDownloads = "20万",
        gameLikes = "17万"
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
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tile_bg)
        val drawable = BitmapDrawable(resources, bitmap)
        drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT)
        drawable.setDither(true)
        drawable.alpha = 80
        contentView.background = drawable

        gameTitle = contentView.findViewById(R.id.gamestap)
        GlideUtil.displayImgByResId(mContext, R.drawable.game_title, gameTitle)

        listGame = ArrayList()
        listGame.add(gameBean0)
        listGame.add(gameBean1)
        listGame.add(gameBean2)
        listGame.add(gameBean3)
        listGame.add(gameBean4)
        gamePageBean = GamePageBean(id = 0L, gameList = listGame)

        gameList = contentView.findViewById(R.id.game_list)
        gameList.layoutManager = GridLayoutManager(mContext, 2)
        detailAdapter = DetailAdapter(mActivity, mContext, R.layout.item_game_detail, listGame)
        gameList.adapter = detailAdapter

        return contentView
    }


    fun refreshList() {
        detailAdapter.refreshAdapter(listGame)
    }
}