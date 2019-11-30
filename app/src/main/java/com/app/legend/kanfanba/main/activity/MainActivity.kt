package com.app.legend.kanfanba.main.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.adapter.MainPagerAdapter
import com.app.legend.kanfanba.main.presenter.IMainActivity
import com.app.legend.kanfanba.main.presenter.MainPresenter
import com.app.legend.kanfanba.play.activity.PlayActivity
import com.app.legend.kanfanba.search.activity.SearchActivity
import com.app.legend.ruminasu.activityes.BasePresenterActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


/**
 * 首页展示，第一页展示番剧，第二页展示剧场版（viewpager）
 * 优先展示最新更新的番剧，以剧集的形式展示，点击后会进入这个番的详细页面，以及所有视频，点击视频直接观看
 *
 */
class MainActivity : BasePresenterActivity<IMainActivity,MainPresenter>(),IMainActivity {

    private lateinit var searchView:SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.getSecurity()//获取安全码

        initToolbar()

        initPager()


    }

    /**
     * 初始化toolbar
     */
    private fun initToolbar(){

        setSupportActionBar(main_toolbar)

    }


    /**
     * 配置菜单，找到searchview，初始化searchview
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)

        val search=menu!!.findItem(R.id.menu_search)

        searchView=search.actionView as SearchView

        initSearch()

        return true
    }

    /**
     * 初始化pager，adapter，同时获取数据
     */
    private fun initPager() {

        val list:MutableList<String> =ArrayList()

        list.add("更新")
        list.add("番剧")
        list.add("剧场版")

        val adapter=MainPagerAdapter(supportFragmentManager,list)

        main_pager.offscreenPageLimit=2

        main_pager.adapter=adapter

        for (i in list){
            main_tab.addTab(main_tab.newTab().setText(i))
        }

        main_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(main_tab))
        main_tab.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(main_pager))

    }

    /**
     * 搜索
     */
    private fun initSearch(){


        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                startSearch(query)

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }


        })

    }


    private fun startSearch(query:String?){

        if (query == null || query.isBlank()){

            return
        }

        val intent=Intent(this,SearchActivity::class.java)
        intent.putExtra("s",query)

        startActivity(intent)

    }


    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun setSecurityValue(s: String) {
        sharedPreferences!!.edit().putString("security",s).apply()
    }

    override fun onError(msg: String) {
        Toast.makeText(this,"获取内容出错，错误原因：$msg",Toast.LENGTH_LONG).show()
    }

    private var currentBackPressedTime: Long = 0
    // 退出间隔
    private val BACK_PRESSED_INTERVAL = 2000

    override fun onBackPressed() {
//        super.onBackPressed()

        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            super.onBackPressed();
        }

    }

    private fun showDialog(){

        val dialog=AlertDialog.Builder(this).setTitle("警告").setMessage("目前正在使用移动网络，这可能会消耗您的流量，产生流量费用，一切以您运营商套餐内容为准。本APP不会以任何形式扣费。").setNegativeButton("确定") { dialog, which ->

            dialog.dismiss()

        }.show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.about->{
                startAbout()
            }

            R.id.about_app->{

                val dialog=AlertDialog.Builder(this).setTitle("警告").setMessage(getString(R.string.about_app_content)).setNegativeButton("确定") { dialog, which ->

                    dialog.dismiss()

                }.show()
            }
        }


        return super.onOptionsItemSelected(item)
    }

    private fun startAbout(){

        val intent=Intent(this,PlayActivity::class.java)

        val video=Video("关于看番吧","","","","","https://xiazailianjie.com/video_post/SOS_batch/index.m3u8",0,"",-1)

        intent.putExtra("video",video)

        startActivity(intent)

    }

}
