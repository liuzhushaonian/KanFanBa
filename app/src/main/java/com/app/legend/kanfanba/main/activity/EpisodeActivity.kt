package com.app.legend.kanfanba.main.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.adapter.EpisodeVideosAdapter
import com.app.legend.kanfanba.main.adapter.OnVideoListItemClick
import com.app.legend.kanfanba.main.presenter.EpisodeActivityPresenter
import com.app.legend.kanfanba.main.presenter.IEpisodeActivity
import com.app.legend.kanfanba.play.activity.PlayActivity
import com.app.legend.ruminasu.activityes.BasePresenterActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_episode.*
import java.net.URLEncoder


class EpisodeActivity : BasePresenterActivity<IEpisodeActivity,EpisodeActivityPresenter>(),IEpisodeActivity {



    private val adapter=EpisodeVideosAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {

        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(Color.TRANSPARENT)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_episode)

        reDraw()

        initToolbar()

        initList()

        initData()

    }

    override fun createPresenter(): EpisodeActivityPresenter {
        return EpisodeActivityPresenter(this)
    }

    private fun initToolbar(){

        ep_toolbar.title=""

        setSupportActionBar(ep_toolbar)

        ep_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        ep_toolbar.setNavigationOnClickListener {

            finish()
        }

        ep_toolbar.background.alpha=16

    }

    private fun initData(){

        val episode=intent.getParcelableExtra<Episode>("episode")

        if (episode!=null){

            presenter.getData(episode)
            ep_toolbar.title=episode.title
            ep_title.text=episode.title

            var ue=URLEncoder.encode(episode.bigBook,"UTF-8")

            ue=ue.replace("%3A",":").replace("%2F","/")

            if (ue.startsWith("+")){

                ue=ue.replace("+","")

            }

//            Log.d("big---->>>",ue)

            Glide.with(this).asBitmap().load(ue).into(episode_book)

        }

    }

    override fun setData(list: MutableList<Video>) {

        spin_kit.visibility=View.GONE

        adapter.initList(list)

    }

    override fun onError(msg: String) {
        Toast.makeText(this,"获取内容出错，错误原因：$msg", Toast.LENGTH_LONG).show()
    }

    private fun initList(){

        val linearLayoutManager=LinearLayoutManager(this)
        list.adapter=adapter
        list.layoutManager=linearLayoutManager

        adapter.onListItemClick=object :OnVideoListItemClick{
            override fun clickItem(video: Video, list: MutableList<Video>) {

                startPlayActivity(video, list as ArrayList<Video>)

            }

        }

    }

    private fun startPlayActivity(video: Video, list: ArrayList<Video>){

        val intent=Intent(this,PlayActivity::class.java)

        intent.putExtra("video",video)
        intent.putParcelableArrayListExtra("list",list)

        startActivity(intent)

    }

    private fun reDraw(){

        val p=episode_book.layoutParams

        val w=resources.displayMetrics.widthPixels

        val h=(w*0.56).toInt()

        p.height=h

        episode_book.layoutParams=p

        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->

            val dis:Int=h/255

            var speed:Int=scrollY/dis

            if (speed>255){
                speed=255
            }

            if (speed<16){
                speed=16
            }

            this.ep_toolbar.background.alpha=speed

        }

    }

    override fun finish() {
        super.finish()
        ep_toolbar.background.alpha=255
    }


}
