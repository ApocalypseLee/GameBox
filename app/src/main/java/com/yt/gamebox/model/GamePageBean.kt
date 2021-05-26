package com.yt.gamebox.model

data class GamePageBean(
    var id: Long,
    var gameList: MutableList<GameBean>,
)