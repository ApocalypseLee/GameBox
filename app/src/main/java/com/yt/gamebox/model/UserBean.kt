package com.yt.gamebox.model

data class UserBean(
    var userName: String,
    var userID: String,
    var binded: Boolean,
    var userAvatar: String,
    var cashTotal: String,
    var cashTakeout: String,
    var newbieBean: NewbieBean,
)
