package com.yt.gamebox.model

import com.yt.gamebox.Adapters.DetailAdapter

data class NewbieBean(
    var type: Int = DetailAdapter.VIEW_TYPE_WALLET,
    var id: Long,
    var newbieCash: String,
    var walletCashLV1: String,
    var walletCostLV1: String,
    var walletCashLV2: String,
    var walletCostLV2: String,
    var walletCashLV3: String,
    var walletCostLV3: String,
)
