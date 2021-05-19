package com.yt.gamebox.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.yt.gamebox.R
import com.yt.gamebox.WebViewActivity
import com.yt.gamebox.Adapters.DetailAdapter

class GameFragment(private val activity: Activity) : Fragment() {
    private lateinit var game_list: ListView
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var pinballBG: ImageView
    private lateinit var pingusBG: ImageView
    private lateinit var expressBG: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val contentView: View = inflater.inflate(R.layout.fragment_game, container, false)

        pinballBG = contentView.findViewById(R.id.pinball_bg)
        pinballBG.setOnClickListener {
            gotoGamePage(0)
        }
        pingusBG = contentView.findViewById(R.id.pingus_bg)
        pingusBG.setOnClickListener {
            gotoGamePage(1)
        }
        expressBG = contentView.findViewById(R.id.express_bg)
        expressBG.setOnClickListener {
            gotoGamePage(2)
        }
        game_list = contentView.findViewById(R.id.game_list)
        game_list.setOnScrollListener(scrollListener)
        detailAdapter = DetailAdapter(activity, activity.applicationContext)
        detailAdapter.setContent()
        game_list.adapter = detailAdapter

        return contentView
    }


    fun gotoGamePage(position: Int) {
        val intent = Intent(activity, WebViewActivity::class.java)
        when (position) {
            0 -> {
                intent.putExtra("title", "砰砰弹力球")
                intent.putExtra("url", "")
            }
            1 -> {
                intent.putExtra("title", "企鹅大战")
                intent.putExtra("url", "")
            }
            2 -> {
                intent.putExtra("title", "快递小哥冲冲冲")
                intent.putExtra("url", "")
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
        var contentView: MutableList<Int> = ArrayList()
        contentView.add(1)
        contentView.add(1)
        detailAdapter.refreshAdapter(contentView)
    }

    fun getGameList(): ListView {
        return game_list
    }
}