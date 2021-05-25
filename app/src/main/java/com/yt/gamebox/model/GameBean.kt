package com.yt.gamebox.model

import com.yt.gamebox.Adapters.DetailAdapter

data class GameBean(
    var type: Int = DetailAdapter.VIEW_TYPE_GAME,
    var id: Long,
    var gameUrl: String,
    var gameIconUrl: String,
    var gameIconResID: Int,
    var gameBGUrl: String,
    var gameBGResID: Int,
    var gameName: String,
    var gameLikes: String,
    var gameDownloads: String,
)