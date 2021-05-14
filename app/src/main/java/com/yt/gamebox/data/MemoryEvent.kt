package com.yt.gamebox.data

data class MemoryEvent(private var memUsed: Double, private var memTotal: Double) {

    fun getUsedPercent(): Int {
        return (memUsed / memTotal * 100).toInt()
    }


}