package com.app.legend.kanfanba.play.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.m3u8.M3u8Downloader
import com.app.legend.kanfanba.play.adapter.OnPlayListItemClick
import com.app.legend.kanfanba.play.adapter.PlayListAdapter
import com.app.legend.kanfanba.play.presenter.IPlayActivity
import com.app.legend.kanfanba.play.presenter.PlayPresenter
import com.app.legend.ruminasu.activityes.BasePresenterActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.exo_player_control_view.*
import kotlinx.android.synthetic.main.exo_player_control_view.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlayActivity : BasePresenterActivity<IPlayActivity, PlayPresenter>(), IPlayActivity {


    lateinit var player: ExoPlayer
    private lateinit var lock1:ImageView
    private lateinit var lock2:ImageView
    private lateinit var next:ImageView
    private var isLock=false
    private var playingVideo: Video?=null
    val adapter=PlayListAdapter()

    private var isLand=false


    override fun onCreate(savedInstanceState: Bundle?) {

//        window.getDecorView().setSystemUiVisibility(
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        )
//
//        window.setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN)
        window.setStatusBarColor(Color.BLACK)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        initPlayer()

        reDraw()

        getVideo()

        event()

    }

    private fun initPlayer() {

        player = ExoPlayerFactory.newSimpleInstance(this)
        player_view.player = player

        val sp=player_view.findViewById(R.id.zoom) as ImageView

        lock1=player_view.findViewById(R.id.lock_1)

        lock2=player_view.findViewById(R.id.lock_2)

        next=player_view.findViewById(R.id.play_next)


        sp.setOnClickListener {

            changeOrientation()

        }

        val gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {

            /**
             * 双击发生时的通知
             * @param e
             * @return
             */
            override fun onDoubleTap(e: MotionEvent): Boolean { //双击事件

                if (isLock){
                    return false
                }

                player.playWhenReady=!player.playWhenReady

                return true
            }

        })



        player_view.setOnTouchListener{

            v: View?, event: MotionEvent? ->

            gestureDetector.onTouchEvent(event)

        }

    }

    override fun createPresenter(): PlayPresenter {
        return PlayPresenter(this)
    }

    /**
     * 获取传过来的video对象，根据对象信息获取m3u8
     */
    private fun getVideo() {

        val v = intent.getParcelableExtra<Video>("video")
        playingVideo=v

        if (v.url.isNotEmpty()){//播放关于看番吧

            setSource(v.url)
            video_title.text="关于看番吧"
            thanks.visibility=View.VISIBLE
            play_list.visibility=View.GONE
            list_title.visibility=View.GONE


        }else {

            thanks.visibility=View.GONE
            play_list.visibility=View.VISIBLE
            list_title.visibility=View.VISIBLE

            initVideoInfos(v)
            initList(v)
        }
    }

    override fun setSource(url: String) {

        playVideo(url)

    }

    private fun playVideo(url: String) {


        val uri = Uri.parse(url)

        val hlsMediaSource = HlsMediaSource.Factory(
            HlsDataSourceFactory { dataType: Int ->
                val dataSource: HttpDataSource =
                    DefaultHttpDataSource(Util.getUserAgent(this, "kanfanba"))

                dataSource.setRequestProperty("referer", "https://akcp.kanfanba.com/")

                dataSource
            }
        )
            .createMediaSource(uri)

        player.prepare(hlsMediaSource)

        player.playWhenReady=true

//        val m3u8Downloader=M3u8Downloader()
//
//        GlobalScope.launch(Dispatchers.IO) {
//            //IO 线程里拉取数据
//
//            m3u8Downloader.download(url)
//
//        }


//        player.playWhenReady = true

        Log.d("asasasxxs------>>>","播放中！！")


    }

    /**
     * 监听屏幕变化
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val o=resources.configuration.orientation

        when(o){

            Configuration.ORIENTATION_LANDSCAPE->{//横屏

                enterFullScreen()

            }

            Configuration.ORIENTATION_PORTRAIT->{//竖屏

                exitsFullScreen()

            }
        }
    }


    /**
     * 把播放器设置成全屏模式
     */
    private fun enterFullScreen(){

        isLand=true

        val container=player_view.parent as ViewGroup
        container.removeView(player_view)

        land_container.visibility=View.VISIBLE

        portrait_container.visibility=View.GONE

        lock1.visibility=View.VISIBLE
        lock2.visibility=View.VISIBLE

        land_container.addView(player_view)

        window.setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN)

    }

    private fun exitsFullScreen(){

        isLand=false
        val container=player_view.parent as ViewGroup
        container.removeView(player_view)

        land_container.visibility=View.GONE

        portrait_container.visibility=View.VISIBLE

        lock1.visibility=View.GONE
        lock2.visibility=View.GONE

        portrait_container.addView(player_view)

        window.clearFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN)

    }


    private fun reDraw(){

        val p=portrait_container.layoutParams

        val w=resources.displayMetrics.widthPixels

        val h=(w * (0.5625f)).toInt()

        p.height=h

        portrait_container.layoutParams=p

    }

    private fun changeOrientation(){

        if (isLock){
            return
        }

        if (requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){//横屏


            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        }else{//竖屏

            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        }

    }

    override fun onBackPressed() {

        if (isLock){

            Toast.makeText(this,"请先解除锁定",Toast.LENGTH_LONG).show()
            return
        } else if (isLand){

            changeOrientation()

        }else{

            super.onBackPressed()
        }
    }

    private fun event(){

        lock1.setOnClickListener {

            if (isLock){

                unlockScreen()

            }else{

                lockScreen()

            }


        }

        lock2.setOnClickListener {

            if (isLock){

                unlockScreen()

            }else{

                lockScreen()

            }

        }

        player_view.con_back.setOnClickListener {

//            if (isLock){
//
//                Toast.makeText(this,"请先解除锁定",Toast.LENGTH_LONG).show()
//
//            }else {
//                exitsFullScreen()
//            }


            if (isLand){//横屏

                changeOrientation()

            }else {

                finish()

            }

        }

        next.setOnClickListener {

            next()

        }

    }


    /**
     * 横屏播放时锁定，不允许显示控件以及返回操作
     */
    private fun lockScreen(){

        isLock=true
        hideLocks()
        player_view.lock_1.setImageResource(R.drawable.ic_lock)
        player_view.lock_2.setImageResource(R.drawable.ic_lock)

    }

    /**
     * 解锁
     * 先隐藏，后显示
     */
    private fun unlockScreen(){

        showLocks()
        isLock=false
        player_view.lock_1.setImageResource(R.drawable.ic_unlock)
        player_view.lock_2.setImageResource(R.drawable.ic_unlock)


    }

    /**
     * 隐藏
     */
    private fun hideLocks(){

        player_view.con_foot.visibility=View.GONE
        player_view.con_header.visibility=View.GONE
    }

    private fun showLocks(){

        player_view.con_foot.visibility=View.VISIBLE
        player_view.con_header.visibility=View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        if (player_view!=null) {
            player_view.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (player_view!=null){

            player_view.onPause()

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        player_view.player=null

        player.stop()

        player.release()

    }


    /**
     * 播放
     */
    private fun initVideoInfos(v:Video){

        video_title.text=v.title
        player_view.vv_title.text=v.title

        val security = sharedPreferences!!.getString("security", "")

        presenter.getM3u8(v, security!!)

    }

    /**
     * 初始化列表
     */
    private fun initList(v:Video){

        var list=intent.getParcelableArrayListExtra<Video>("list")



        val linearLayoutManager=LinearLayoutManager(this)

        if (list==null||list.isEmpty()){

            list = ArrayList()
            list.add(v)

        }

        play_list.adapter=adapter
        play_list.layoutManager=linearLayoutManager

        adapter.initList(list)

        adapter.itemClick=object :OnPlayListItemClick{
            override fun clickItem(position: Int, v: Video) {

                initVideoInfos(v)

            }

        }

    }

    /**
     * 下一集
     */
    private fun next(){

        if (playingVideo==null){

            return

        }

        val v=adapter.getNext(playingVideo)

        if (v!=null){
            playingVideo=v

            initVideoInfos(playingVideo!!)

        }else{

            Toast.makeText(this,"已经是最后一集了",Toast.LENGTH_LONG).show()
        }

    }

}