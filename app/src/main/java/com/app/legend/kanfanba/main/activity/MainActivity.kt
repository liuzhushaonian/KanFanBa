package com.app.legend.kanfanba.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.main.adapter.MainPagerAdapter
import com.app.legend.kanfanba.main.presenter.IMainActivity
import com.app.legend.kanfanba.main.presenter.MainPresenter
import com.app.legend.ruminasu.activityes.BaseActivity
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



    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun setSecurityValue(s: String) {
        sharedPreferences!!.edit().putString("security",s).apply()
    }




}
