package com.yt.gamebox.Utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.yt.gamebox.Utils.FloatWindowParamManager.TAG
import java.lang.reflect.Method
import java.util.*


object SystemProperties {
    private val getStringProperty = getMethod(getClass("android.os.SystemProperties"))

    private fun getClass(name: String): Class<*>? {
        return try {
            val cls = Class.forName(name) ?: throw ClassNotFoundException()
            cls
        } catch (e: ClassNotFoundException) {
            try {
                ClassLoader.getSystemClassLoader().loadClass(name)
            } catch (e1: ClassNotFoundException) {
                null
            }
        }
    }

    private fun getMethod(clz: Class<*>?): Method? {
        return if (clz == null) null else try {
            clz.getMethod("get", String::class.java)
        } catch (e: Exception) {
            null
        }
    }

    operator fun get(key: String?): String {
        if (getStringProperty != null) {
            try {
                val value = getStringProperty.invoke(null, key) ?: return ""
                return trimToEmpty(value.toString())
            } catch (ignored: Exception) {
            }
        }
        return ""
    }

    operator fun get(key: String?, def: String): String {
        if (getStringProperty != null) {
            try {
                val value = getStringProperty.invoke(null, key) as String
                return defaultString(trimToNull(value), def)
            } catch (ignored: Exception) {
            }
        }
        return def
    }

    private fun defaultString(str: String?, defaultStr: String): String {
        return str ?: defaultStr
    }

    private fun trimToNull(str: String): String? {
        val ts = trim(str)
        return if (TextUtils.isEmpty(ts)) null else ts
    }

    private fun trimToEmpty(str: String): String {
        return str.trim { it <= ' ' } ?: ""
    }

    private fun trim(str: String): String {
        return str.trim { it <= ' ' }
    }

    fun getAvailableMemory(activity: Activity): Long {
        // 获取android当前可用内存大小
        val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val mi = ActivityManager.MemoryInfo()
        am!!.getMemoryInfo(mi)
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        Log.d(TAG, "可用内存---->>>" + mi.availMem / (1024 * 1024))
        return mi.availMem / (1024 * 1024)
    }

    fun getTotalMemory(activity: Activity): Long {
        val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val mi = ActivityManager.MemoryInfo()
        am!!.getMemoryInfo(mi)
        Log.d(TAG, "总内存---->>>" + mi.totalMem / (1024 * 1024))
        return mi.totalMem / (1024 * 1024)
    }

    fun getUsedMemory(activity: Activity): Long {
        val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val mi = ActivityManager.MemoryInfo()
        am!!.getMemoryInfo(mi)
        Log.d(TAG, "已用内存---->>>" + (mi.totalMem - mi.availMem) / (1024 * 1024))
        return (mi.totalMem - mi.availMem) / (1024 * 1024)
    }

    fun isLowMemory(activity: Activity): Boolean {
        val am = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val mi = ActivityManager.MemoryInfo()
        am!!.getMemoryInfo(mi)
        return mi.lowMemory
    }

    fun getUsedPercentValueInt(activity: Activity): Int {
        val used = getUsedMemory(activity)
        val total = getTotalMemory(activity)
        return (used.toDouble() / total.toDouble() * 100).toInt()
    }

    //生成15位唯一性的订单号
    fun getUUID(): String {
        //随机生成一位整数
        val random = (Math.random() * 9 + 1).toInt()
        val valueOf = random.toString()
        //生成uuid的hashCode值
        var hashCode = UUID.randomUUID().toString().hashCode()
        //可能为负数
        if (hashCode < 0) {
            hashCode = -hashCode
        }
        return valueOf + String.format("%014d", hashCode)
    }
}