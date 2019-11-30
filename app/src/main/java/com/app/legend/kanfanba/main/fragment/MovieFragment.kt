package com.app.legend.kanfanba.main.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.adapter.MovieAdapter
import com.app.legend.kanfanba.main.adapter.OnMovieItemClick
import com.app.legend.kanfanba.main.presenter.IMovieFragment
import com.app.legend.kanfanba.main.presenter.MovieFragmentPresenter
import com.app.legend.kanfanba.play.activity.PlayActivity
import com.app.legend.kanfanba.utils.CourseSearchAdapterWrapper
import com.app.legend.kanfanba.utils.MovieSpace
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.app.legend.ruminasu.fragments.BasePresenterFragment
import com.github.ybq.android.spinkit.SpinKitView

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : BasePresenterFragment<IMovieFragment,MovieFragmentPresenter>(),IMovieFragment {


    private val adapter=MovieAdapter()

    lateinit var list:RecyclerView
    lateinit var swipe:SwipeRefreshLayout

    private var p=1

    private var vp=1

    private var bottom=false

    private var refresh=false

    private lateinit var spin_kit: SpinKitView

    var courseSearchAdapterWrapper: CourseSearchAdapterWrapper?=null

    override fun createPresenter(): MovieFragmentPresenter {

        return MovieFragmentPresenter(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_main_movie, container, false)
        getComponent(view)
        initList()
        initSwipe()

        getData(p)

        return view
    }

    private fun getComponent(view: View){

        list=view.findViewById(R.id.movie_list)
        swipe=view.findViewById(R.id.movie_swipe)
        spin_kit=view.findViewById(R.id.spin_kit)
    }

    private fun getData(page:Int){

        presenter.getData(page)

    }

    private fun initSwipe(){

        swipe.setProgressViewOffset(true,20,200)

        swipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        swipe.setOnRefreshListener {

            refresh=true

            p=1//重置p

            vp=p

            getData(p)

        }
    }


    private fun initList(){

        val gridLayoutManager=GridLayoutManager(context,3)

        list.layoutManager=gridLayoutManager

//        list.adapter=adapter

        list.addItemDecoration(MovieSpace())

        courseSearchAdapterWrapper=
            CourseSearchAdapterWrapper(context!!,adapter as BaseAdapter<RecyclerView.ViewHolder>,24,list)

        courseSearchAdapterWrapper?.span=3

        courseSearchAdapterWrapper?.setOnLoadMoreListener(object :CourseSearchAdapterWrapper.OnLoadMoreListener{
            override fun onLoadMore() {

                if (!list.canScrollVertically(1)){//到底部

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

        list.adapter=courseSearchAdapterWrapper


        adapter.onMovieItemClick=object :OnMovieItemClick{
            override fun itemClick(video: Video) {

                startPlayActivity(video)

            }
        }
    }

    private fun startPlayActivity(video: Video){

        val intent=Intent(context,PlayActivity::class.java)
        intent.putExtra("video",video)

        startActivity(intent)

    }


    override fun setData(list: MutableList<Video>) {

        spin_kit.visibility=View.GONE

        if (list.isNotEmpty()) {

            if (refresh){

                adapter.clean()

                Toast.makeText(context,"已获取最新数据", Toast.LENGTH_SHORT).show()
                refresh=false
                swipe.isRefreshing=refresh

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

    override fun onError(msg: String) {
        Toast.makeText(context,"获取内容出错，错误原因：$msg",Toast.LENGTH_LONG).show()
    }


}
