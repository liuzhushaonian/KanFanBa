package com.app.legend.kanfanba.play.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.play.adapter.OnPlayListItemClick
import com.app.legend.kanfanba.play.adapter.PlayListAdapter
import com.app.legend.kanfanba.play.presenter.IPlayActivity
import com.app.legend.kanfanba.play.presenter.PlayPresenter
import com.app.legend.ruminasu.activityes.BasePresenterActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.exo_player_control_view.*
import kotlinx.android.synthetic.main.exo_player_control_view.view.*


class PlayActivity : BasePresenterActivity<IPlayActivity, PlayPresenter>(), IPlayActivity {


    lateinit var player: ExoPlayer
    private lateinit var lock1:ImageView
    private lateinit var lock2:ImageView

    private var isLock=false

    override fun onCreate(savedInstanceState: Bundle?) {
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

        initVideoInfos(v)
        initList(v)



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

        if (requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){//横屏


            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        }else{//竖屏

            requestedOrientation=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        }

    }

    override fun onBackPressed() {

        if (requestedOrientation==ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){

            if (isLock){

                Toast.makeText(this,"请先解除锁定",Toast.LENGTH_LONG).show()

                return
            }

            exitsFullScreen()

        }else {

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

            if (isLock){

                Toast.makeText(this,"请先解除锁定",Toast.LENGTH_LONG).show()

            }else {
                exitsFullScreen()
            }

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


    private fun initVideoInfos(v:Video){

        video_title.text=v.title
        player_view.vv_title.text=v.title

        val security = sharedPreferences!!.getString("security", "")

        presenter.getM3u8(v, security!!)

    }

    private fun initList(v:Video){

        var list=intent.getParcelableArrayListExtra<Video>("list")

        val adapter=PlayListAdapter()

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





}
