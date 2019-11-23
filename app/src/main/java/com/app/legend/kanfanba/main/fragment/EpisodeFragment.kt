package com.app.legend.kanfanba.main.fragment


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.adapter.EpisodeAdapter
import com.app.legend.kanfanba.main.presenter.EpisodeFragmentPresenter
import com.app.legend.kanfanba.main.presenter.IEpisodeFragment
import com.app.legend.ruminasu.fragments.BasePresenterFragment
import com.app.legend.ruminasu.utils.MainItemSpace

/**
 * A simple [Fragment] subclass.
 */
class EpisodeFragment : BasePresenterFragment<IEpisodeFragment,EpisodeFragmentPresenter>(),IEpisodeFragment {


    lateinit var ep_list: RecyclerView
    lateinit var ep_swipe: SwipeRefreshLayout
    val adapter=EpisodeAdapter()
    private var p=1

    private var vp=1

    private var bottom=false

    private var refresh=false

    override fun createPresenter(): EpisodeFragmentPresenter {
        return EpisodeFragmentPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_main_episode, container, false)

        getComponent(view)

        initList()

        initSwipe()

        getData(p)

        return view
    }



    private fun getComponent(view: View){

        ep_list=view.findViewById(R.id.episode_list)
        ep_swipe=view.findViewById(R.id.ep_swipe)

    }

    private fun getData(page:Int){

        presenter.getData(page)

    }

    private fun initSwipe(){

        ep_swipe.setProgressViewOffset(true,20,200)

        ep_swipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        ep_swipe.setOnRefreshListener {

            refresh=true

            p=1//重置p

            vp=p

            getData(p)

        }
    }

    private fun initList(){

        val gridLayoutManager = GridLayoutManager(context,2)

        ep_list.adapter=adapter
        ep_list.layoutManager=gridLayoutManager
        ep_list.addItemDecoration(MainItemSpace())

        ep_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!ep_list.canScrollVertically(1)){//到底部

                    if (vp!=p){//判断是否加载过，如果是，则不加载

                        return

                    }

                    if (bottom){
                        return
                    }

                    getData(p)//获取下一页数据


                }


            }
        })


        adapter.onEpisodeItemClick=object :OnEpisodeItemClick{
            override fun click(episode: Episode) {




            }


        }


    }

    override fun setData(list: MutableList<Episode>) {

        if (list.isNotEmpty()) {

            if (refresh){

                adapter.clean()

                Toast.makeText(context,"已获取最新数据", Toast.LENGTH_SHORT).show()
                refresh=false
                ep_swipe.isRefreshing=refresh

            }

            adapter.addList(list)

            p += 1//添加一次，方便获取下一页

            vp=p

            bottom=false

        }else{//获得的列表是空的

            bottom=true

        }


    }


}
