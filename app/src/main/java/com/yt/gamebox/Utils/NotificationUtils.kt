package com.yt.gamebox.Utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.blankj.utilcode.util.AppUtils
import com.yt.gamebox.AdActivity
import com.yt.gamebox.AlarmActivity
import com.yt.gamebox.R

class NotificationUtils(base: Context?) : ContextWrapper(base) {

    val TAG = NotificationUtils::class.java.simpleName

    val id = "channel_1"
    val name = "notification"
    val flag = "flag"
    val resId = "resId"
    private var manager: NotificationManager? = null
    private var mContext: Context? = null
    private var resID = 0

    init {
        mContext = base
        getManager(this)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun createNotificationChannel(id: String?, name: String?, importance: Int) {
        val channel = NotificationChannel(id, name, importance)
        getManager(this)!!.createNotificationChannel(channel)
    }

    fun getManager(context: Context): NotificationManager? {
        if (manager == null) {
            manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager
    }

    fun sendNotificationFullScreen(
        targetClass: Class<*>,
        title: String?,
        content: String?,
        type: String,
        resID: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.resID = resID
            createNotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            val notification = getChannelNotificationQ(targetClass, title, content, type)

            if (targetClass.simpleName == AdActivity::class.java.getSimpleName()) {
                notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
                notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
            }

            if (targetClass.simpleName == AlarmActivity::class.java.getSimpleName()) {
                val remoteViews = RemoteViews(packageName, R.layout.notification)
                initRes(resources, remoteViews, resID, type)
                notification.contentView = remoteViews
            } else if (type == Intent.ACTION_BOOT_COMPLETED) {
                val remoteViews = RemoteViews(packageName, R.layout.notification)
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.hint_iphone)
                remoteViews.setImageViewBitmap(R.id.notify_bg, bitmap)
                notification.contentView = remoteViews
                notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
                notification.flags = notification.flags or Notification.FLAG_NO_CLEAR
            }
            getManager(this)!!.notify(1, notification)
        }
    }

    fun clearAllNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getChannelNotificationQ(
        targetClass: Class<*>?,
        title: String?,
        content: String?,
        type: String?
    ): Notification {
        val fullScreenIntent = Intent(this, targetClass)
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        fullScreenIntent.putExtra(flag, type)
        fullScreenIntent.putExtra(resId, resID)
        val fullScreenPendingIntent =
            PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setTicker(content)
            .setContentText(content)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(Notification.CATEGORY_CALL)
            .setFullScreenIntent(fullScreenPendingIntent, true)
        return notificationBuilder.build()
    }

    private fun initRes(resources: Resources, remoteViews: RemoteViews, resID: Int, type: String) {
        var bitmap: Bitmap? = null
        if (type == Intent.ACTION_SCREEN_OFF) {
            when (resID) {
                1 -> bitmap = BitmapFactory.decodeResource(resources, R.drawable.hint_iphone)
                2 -> bitmap = BitmapFactory.decodeResource(resources, R.drawable.hint_bonus)
                3 -> bitmap = BitmapFactory.decodeResource(resources, R.drawable.hint_money2)
            }
        } else if (type == Intent.ACTION_TIME_TICK) {
            when (resID) {
                1 -> bitmap = BitmapFactory.decodeResource(resources, R.drawable.alarm_iphone)
                2 -> bitmap = BitmapFactory.decodeResource(resources, R.drawable.alarm_money)
            }
        }
        if (bitmap != null) remoteViews.setImageViewBitmap(R.id.notify_bg, bitmap)
    }


    companion object {
        fun getForegroundNotification(
            context: Context,
            channelId: String,
            title: String?,
            content: String?,
            bitmap: Bitmap?
        ): Notification? {
            createNotificationChannelForeground(channelId, context)
            val mBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(UIUtils.getBitmapFromDrawable(context, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
            val msgIntent = getStartAppIntent(context.applicationContext)
            val mainPendingIntent = PendingIntent.getActivity(
                context.applicationContext, 0,
                msgIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val notification =
                mBuilder.setContentIntent(mainPendingIntent).setAutoCancel(false).build()
            if (bitmap != null) {
                val remoteViews = RemoteViews(context.packageName, R.layout.notification)
                remoteViews.setImageViewBitmap(R.id.notify_bg, bitmap)
                notification.contentView = remoteViews
            }
            notification.flags =
                notification.flags or Notification.FLAG_ONGOING_EVENT //??????????????????????????????"Ongoing"???"????????????"??????
            notification.flags =
                notification.flags or Notification.FLAG_NO_CLEAR //?????????????????????????????????"????????????"?????????????????????????????????FLAG_ONGOING_EVENT????????????
            return notification
        }

        fun getStartAppIntent(context: Context): Intent? {
            val intent = context.packageManager
                .getLaunchIntentForPackage(AppUtils.getAppPackageName())
            intent?.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            )
            return intent
        }

        fun createNotificationChannelForeground(channelId: String, context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = "Name"
                val description = "Description"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance)
                channel.description = description
                channel.setShowBadge(false)
                val notificationManager = context.getSystemService(
                    NotificationManager::class.java
                )
                notificationManager?.createNotificationChannel(channel)
            }
        }


        /**
         * ??????????????????????????????????????????
         *
         * @param context
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun notificationGuide(context: Context) {
            if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                Toast.makeText(context, "????????????????????????", Toast.LENGTH_LONG).show()
                try {
                    // ??????isOpened?????????????????????????????????????????????AppInfo??????????????????App????????????
                    val intent = Intent()
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    //????????????????????? API 26, ???8.0??????8.0??????????????????
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        intent.putExtra(Notification.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //????????????????????? API21??????25?????? 5.0??????7.1 ???????????????????????????
                        intent.putExtra("app_package", context.packageName)
                        intent.putExtra("app_uid", context.applicationInfo.uid)
                    }
                    // ??????6 -MIUI9.6-8.0.0??????????????????????????????????????????????????????"????????????????????????"??????????????????????????????????????????????????????????????????I'm not ok!!!
                    //  if ("MI 6".equals(Build.MODEL)) {
                    //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //      Uri uri = Uri.fromParts("package", getPackageName(), null);
                    //      intent.setData(uri);
                    //      // intent.setAction("com.android.settings/.SubSettings");
                    //  }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // ?????????????????????????????????????????????????????????3??????OC105 API25
                    val intent = Intent()

                    //??????????????????????????????????????????????????????????????????
                    //https://blog.csdn.net/ysy950803/article/details/71910806
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    context.startActivity(intent)
                }
            }
        }
    }

}