package com.yt.gamebox.Services

import com.yt.gamebox.model.GameBean
import com.yt.gamebox.model.GamePageBean
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface RequestService {
    @GET("wxarticle/chapters/json")
    fun getGameDatas() : Deferred<GamePageBean>


    @GET("wxarticle/chapters/json")
    fun getGameData() : Deferred<GameBean>
}