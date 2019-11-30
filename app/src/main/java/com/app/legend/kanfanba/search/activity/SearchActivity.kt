package com.app.legend.kanfanba.search.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.activity.EpisodeActivity
import com.app.legend.kanfanba.main.adapter.EpisodeAdapter
import com.app.legend.kanfanba.main.adapter.NewestAdapter
import com.app.legend.kanfanba.main.fragment.OnEpisodeItemClick
import com.app.legend.kanfanba.main.fragment.OnNewestItemClick
import com.app.legend.kanfanba.play.activity.PlayActivity
import com.app.legend.kanfanba.search.presenter.ISearchActivity
import com.app.legend.kanfanba.search.presenter.SearchPresenter
import com.app.legend.ruminasu.activityes.BasePresenterActivity
import com.app.legend.ruminasu.utils.MainItemSpace
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BasePresenterActivity<ISearchActivity,SearchPresenter>(),ISearchActivity {

    private lateinit var searchView: SearchView

    private val e_adapter:EpisodeAdapter= EpisodeAdapter()

    private val v_adapter=NewestAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()
        initList()
    }

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter(this)
    }

    private fun initSearch(){

        val key=intent.getStringExtra("s")

//        presenter.startSearch(key!!)


        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query==null||query.isBlank()){

                    return false
                }

                presenter.startSearch(query)

                search_kit.visibility=View.VISIBLE

                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }


        })

        searchView.setQuery(key,true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search_menu,menu)

        val search=menu!!.findItem(R.id.s_search)

        searchView=search.actionView as SearchView

        searchView.isActivated=true

        initSearch()

        return true
    }

    private fun initToolbar(){

        search_toolbar.title=""
        setSupportActionBar(search_toolbar)
        search_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        search_toolbar.setNavigationOnClickListener {

            finish()
        }

    }

    private fun initList(){

        val e_grid=GridLayoutManager(this,2)

        val v_grid=GridLayoutManager(this,2)

        eps_list.adapter=e_adapter
        eps_list.layoutManager=e_grid
        eps_list.isNestedScrollingEnabled=false
        eps_list.addItemDecoration(MainItemSpace())


        vid_list.adapter=v_adapter
        vid_list.layoutManager=v_grid
        vid_list.isNestedScrollingEnabled=false
        vid_list.addItemDecoration(MainItemSpace())

        e_adapter.onEpisodeItemClick=object :OnEpisodeItemClick{
            override fun click(episode: Episode) {
                startEpisodeActivity(episode)
            }
        }

        v_adapter.onNewestItemClick=object :OnNewestItemClick{
            override fun clickItem(position: Int, video: Video) {
                startPlayVideo(video)
            }
        }

    }

    private fun startEpisodeActivity(episode: Episode){

        val intent= Intent(this, EpisodeActivity::class.java)
        intent.putExtra("episode",episode)
        startActivity(intent)

    }

    private fun startPlayVideo(video: Video){

        val intent=Intent(this, PlayActivity::class.java)

        intent.putExtra("video",video)

        startActivity(intent)

    }

    override fun setEpisodeList(list: MutableList<Episode>) {

        search_kit.visibility=View.GONE

        if (list.isNotEmpty()){
            infos.visibility=View.GONE
            eps_list.visibility=View.VISIBLE
            ju.visibility=View.VISIBLE

            e_adapter.addList(list)

        }else{
            eps_list.visibility=View.GONE
            ju.visibility=View.GONE
            infos.visibility=View.VISIBLE
        }


    }

    override fun setVideoList(list: MutableList<Video>) {

        search_kit.visibility=View.GONE

        if (list.isNotEmpty()){

            infos.visibility=View.GONE
            vid_list.visibility=View.VISIBLE
            shi.visibility=View.VISIBLE

            v_adapter.addVideos(list)

        }else{

            vid_list.visibility=View.GONE
            shi.visibility=View.GONE
            infos.visibility=View.VISIBLE
        }

    }

    override fun onError(msg: String) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
    }

}
