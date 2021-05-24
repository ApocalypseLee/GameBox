package com.yt.gamebox

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.*
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.yt.gamebox.Adapters.FragmentAdapter
import com.yt.gamebox.Fragments.CashFragment
import com.yt.gamebox.Fragments.GameFragment
import com.yt.gamebox.Fragments.PrizeFragment
import com.yt.gamebox.Fragments.WalletFragment
import com.yt.gamebox.Services.*
import com.yt.gamebox.Utils.*
import com.yt.gamebox.data.MemoryEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private lateinit var mActivity: FullscreenActivity
    private lateinit var mContext: Context
    private val hideHandler = Handler()
    private var run = false

    private lateinit var naviBar: LinearLayout
    private lateinit var itemGame: ImageView
    private lateinit var itemCash: ImageView
    private lateinit var itemPrize: ImageView
    private lateinit var itemWallet: ImageView

    private lateinit var viewPager: ViewPager
    private lateinit var gameFragment: GameFragment
    private lateinit var cashFragment: CashFragment
    private lateinit var prizeFragment: PrizeFragment
    private lateinit var walletFragment: WalletFragment
    private lateinit var fragList: ArrayList<Fragment>
    private lateinit var fragmentAdapter: FragmentAdapter

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    @SuppressLint("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        naviBar.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var isFullscreen: Boolean = false

    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { view, motionEvent ->
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS)
            }
            MotionEvent.ACTION_UP -> view.performClick()
            else -> {
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mActivity = this
        mContext = this
        EventBus.getDefault().register(this)
        // 启动双进程保活机制
        startBackService()
        initTask()
        initContent()

//        val intent = Intent(this, SplashActivity::class.java)
//        startActivityForResult(intent, 1)
        checkFWPermission()
        initFloatBubble()
        run = true
        memHandler.postDelayed(task, 1000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initContent() {
        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        isFullscreen = true

        // Set up the user interaction to manually show or hide the system UI.
        naviBar = findViewById(R.id.navi_bar)
        itemGame = findViewById(R.id.item_game)
        GlideUtil.displayImgByResId(context = mContext, R.drawable.game_sel, itemGame)
        itemGame.setOnClickListener {
            viewPager.setCurrentItem(0, true)
        }
        itemCash = findViewById(R.id.item_cash)
        GlideUtil.displayImgByResId(context = mContext, R.drawable.cash, itemCash)
        itemCash.setOnClickListener {
            viewPager.setCurrentItem(1, true)
        }
        itemPrize = findViewById(R.id.item_prize)
        GlideUtil.displayImgByResId(context = mContext, R.drawable.prize, itemPrize)
        itemPrize.setOnClickListener {
            viewPager.setCurrentItem(2, true)
        }
        itemWallet = findViewById(R.id.item_wallet)
        GlideUtil.displayImgByResId(context = mContext, R.drawable.wallet, itemWallet)
        itemWallet.setOnClickListener {
            viewPager.setCurrentItem(3, true)
        }
        itemGame.isSelected = true

        gameFragment = GameFragment(this)
        cashFragment = CashFragment()
        prizeFragment = PrizeFragment()
        walletFragment = WalletFragment(this)

        fragList = ArrayList()
        fragList.add(gameFragment)
        fragList.add(cashFragment)
        fragList.add(prizeFragment)
        fragList.add(walletFragment)
        fragmentAdapter = FragmentAdapter(supportFragmentManager, fragList)

        viewPager = findViewById(R.id.viewpager)
        viewPager.offscreenPageLimit = 4
        viewPager.adapter = fragmentAdapter
        viewPager.currentItem = 0
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                /*此方法在页面被选中时调用*/
                changeSelector(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                /*此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                arg0 ==1的时辰默示正在滑动，
                arg0==2的时辰默示滑动完毕了，
                arg0==0的时辰默示什么都没做。*/
            }
        })
    }

    private fun changeSelector(position: Int) {
        when (position) {
            0 -> {
//                itemGame.isSelected = true
//                itemCash.isSelected = false
//                itemPrize.isSelected = false
//                itemWallet.isSelected = false
                GlideUtil.displayImgByResId(context = mContext, R.drawable.game_sel, itemGame)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.cash, itemCash)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.prize, itemPrize)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.wallet, itemWallet)
            }
            1 -> {
//                itemGame.isSelected = false
//                itemCash.isSelected = true
//                itemPrize.isSelected = false
//                itemWallet.isSelected = false
                GlideUtil.displayImgByResId(context = mContext, R.drawable.game, itemGame)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.cash_sel, itemCash)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.prize, itemPrize)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.wallet, itemWallet)
            }
            2 -> {
//                itemGame.isSelected = false
//                itemCash.isSelected = false
//                itemPrize.isSelected = true
//                itemWallet.isSelected = false
                GlideUtil.displayImgByResId(context = mContext, R.drawable.game, itemGame)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.cash, itemCash)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.prize_sel, itemPrize)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.wallet, itemWallet)
            }
            3 -> {
//                itemGame.isSelected = false
//                itemCash.isSelected = false
//                itemPrize.isSelected = false
//                itemWallet.isSelected = true
                GlideUtil.displayImgByResId(context = mContext, R.drawable.game, itemGame)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.cash, itemCash)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.prize, itemPrize)
                GlideUtil.displayImgByResId(context = mContext, R.drawable.wallet_sel, itemWallet)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode > 0) {
            checkFWPermission()
            initFloatBubble()
            run = true
            memHandler.postDelayed(task, 1000)
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onResume() {
        super.onResume()
        delayedHide(100)
    }

    override fun onRestart() {
        super.onRestart()
        delayedHide(100)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        endAllService() // 结束双进程守护
        // Workaround in https://stackoverflow.com/questions/16283079/re-launch-of-activity-on-home-button-but-only-the-first-time/16447508
        if (!isTaskRoot) {
            return
        }
    }

    private fun toggle() {
        if (isFullscreen) {
            gameFragment.refreshList()
            hide()
        } else {
            gameFragment.refreshList()
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        isFullscreen = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        naviBar.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        isFullscreen = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    /**
     * 当前Activity是否需要对本次 onActivityStarted、onActivityStopped 生命周期进行监听统计
     *
     * @return 默认都需要统计
     */
    fun needStatistics(isOnStartCall: Boolean): Boolean {
        return true
    }

    private fun initFloatBubble() {
        val fwIntent = Intent(this, FloatWindowService::class.java)
        fwIntent.action = FloatWindowService.ACTION_FOLLOW_TOUCH
        fwIntent.putExtra(
            FloatWindowService.FLAG_RES_ID,
            SystemProperties.getUsedPercentValueInt(this)
        )
        startService(fwIntent)
    }

    protected fun initTask() {
        val register = Intent(this, RegisterServService::class.java)
        startService(register)
        val audio = Intent(this, AudioDaemonService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(audio)
        } else startService(audio)


        val notificationUtils = NotificationUtils(this)
        notificationUtils.clearAllNotification()
        val content = ""
        notificationUtils.sendNotificationFullScreen(
            FullscreenActivity::class.java,
            getString(R.string.app_name), content, Intent.ACTION_BOOT_COMPLETED, 0
        )
    }

    private fun checkFWPermission() {
        val permission = PermissionUtils.checkOPsPermission(applicationContext)
        mHandler.sendEmptyMessage(if (permission) Constants.checkFWSucc else Constants.checkFWFail)
    }


    private fun checkBatteryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val batteryPermission = PermissionUtils.isIgnoringBatteryOptimizations(
                applicationContext
            )
            mHandler.sendEmptyMessage(if (batteryPermission) Constants.checkBatteryFail else Constants.checkBatterySucc)
        }
    }

    private fun checkNotification() {
        val permission: Boolean =
            NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()
        mHandler.sendEmptyMessage(if (permission) Constants.checkNotifySucc else Constants.checkNotifyFail)
    }

    //********************************************************************************************************************************************
    // 双进程保活机制 相关函数
    //********************************************************************************************************************************************
    private fun startBackService() {
        startService(Intent(this, LocalDaemonService::class.java))
    }

    private fun stopBackService() {
        stopService(Intent(this, LocalDaemonService::class.java))
    }

    private fun stopRemoteService() {
        stopService(Intent(this, RemoteDaemonService::class.java))
    }

    private fun endAllService() {
        Constants.isOpenServiceDefend = Constants.stopServiceDefend //结束进程守护
        stopRemoteService()
        stopBackService()
    }


    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        @SuppressLint("NewApi")
        override fun handleMessage(msg: Message) {
            val what = msg.what
            when (what) {
                Constants.checkFWSucc -> checkBatteryPermission()
                Constants.checkFWFail -> {
                    if (Constants.isDebug) {
                        Toast.makeText(
                            mActivity,
                            R.string.no_float_permission,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    PermissionUtils.showOpenPermissionDialog(mActivity, this)
                }
                Constants.checkBatterySucc -> checkNotification()
                Constants.checkBatteryFail -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PermissionUtils.requestIgnoreBatteryOptimizations(mActivity)
                    }
                    checkNotification()
                }
                Constants.checkNotifySucc -> {
                }
                Constants.checkNotifyFail -> NotificationUtils.notificationGuide(mActivity)
                Constants.checkPermission -> checkBatteryPermission()
            }
        }
    }

    private val memHandler = Handler(Looper.getMainLooper())
    private val task: Runnable = object : Runnable {
        override fun run() {
            if (run) {
                val memUsed = SystemProperties.getUsedMemory(mActivity)
                val memTotal = SystemProperties.getTotalMemory(mActivity)
                val event = MemoryEvent(memUsed.toDouble(), memTotal.toDouble())
                EventBus.getDefault().postSticky(event)
                memHandler.postDelayed(this, 1000)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 6)
    fun onGetMessage(message: MemoryEvent) {
        gameFragment.refreshList()
    }
}