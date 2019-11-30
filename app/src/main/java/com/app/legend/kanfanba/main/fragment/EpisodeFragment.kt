package com.app.legend.kanfanba.main.fragment


import android.content.Intent
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
import com.app.legend.kanfanba.main.activity.EpisodeActivity
import com.app.legend.kanfanba.main.adapter.EpisodeAdapter
import com.app.legend.kanfanba.main.presenter.EpisodeFragmentPresenter
import com.app.legend.kanfanba.main.presenter.IEpisodeFragment
import com.app.legend.kanfanba.utils.CourseSearchAdapterWrapper
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.app.legend.ruminasu.fragments.BasePresenterFragment
import com.app.legend.ruminasu.utils.MainItemSpace
import com.github.ybq.android.spinkit.SpinKitView

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

    private lateinit var spin_kit: SpinKitView

    var courseSearchAdapterWrapper: CourseSearchAdapterWrapper?=null

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
        spin_kit=view.findViewById(R.id.spin_kit)

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

//        ep_list.adapter=adapter
        ep_list.layoutManager=gridLayoutManager
        ep_list.addItemDecoration(MainItemSpace())

        courseSearchAdapterWrapper=CourseSearchAdapterWrapper(context!!,adapter as BaseAdapter<RecyclerView.ViewHolder>,24,ep_list)

        courseSearchAdapterWrapper?.setOnLoadMoreListener(object :CourseSearchAdapterWrapper.OnLoadMoreListener{
            override fun onLoadMore() {

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

        ep_list.adapter=courseSearchAdapterWrapper


        adapter.onEpisodeItemClick=object :OnEpisodeItemClick{
            override fun click(episode: Episode) {


                startEpisodeActivity(episode)

            }


        }


    }

    override fun setData(list: MutableList<Episode>) {

        spin_kit.visibility=View.GONE

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

            courseSearchAdapterWrapper?.notifyDataSetChanged()

        }else{//获得的列表是空的

            bottom=true

            courseSearchAdapterWrapper?.noMoreData()

        }


    }

    override fun onError(msg:String) {
        Toast.makeText(context,"数据获取出错---出错原因：$msg",Toast.LENGTH_LONG).show()
    }

    private fun startEpisodeActivity(episode: Episode){

        val intent=Intent(context,EpisodeActivity::class.java)
        intent.putExtra("episode",episode)
        startActivity(intent)

    }

}
