package com.app.legend.kanfanba.main.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.main.adapter.NewestAdapter
import com.app.legend.kanfanba.main.presenter.INewestFragment
import com.app.legend.kanfanba.main.presenter.NewestFragmentPresenter
import com.app.legend.kanfanba.play.activity.PlayActivity
import com.app.legend.ruminasu.fragments.BasePresenterFragment
import com.app.legend.ruminasu.utils.MainItemSpace
import kotlinx.android.synthetic.main.fragment_main_newest.*

/**
 * A simple [Fragment] subclass.
 */
class NewestFragment : BasePresenterFragment<INewestFragment,NewestFragmentPresenter>(),INewestFragment{


    val adapter=NewestAdapter()

    lateinit var new_list:RecyclerView
    lateinit var new_swipe:SwipeRefreshLayout

    private var p=1

    private var vp=1

    private var bottom=false

    private var refresh=false


    override fun setData(list: MutableList<Video>) {



        if (list.isNotEmpty()) {

            if (refresh){

                adapter.clean()

                Toast.makeText(context,"已获取最新数据",Toast.LENGTH_SHORT).show()
                refresh=false
                new_swipe.isRefreshing=refresh

            }

            adapter.addVideos(list)

            p += 1//添加一次，方便获取下一页

            vp=p

            bottom=false



        }else{//获得的列表是空的

            bottom=true

        }
    }


    override fun createPresenter(): NewestFragmentPresenter {
        return NewestFragmentPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        val view=inflater.inflate(R.layout.fragment_main_newest, container, false)

        getComponent(view)

        initSwipe()

        initList()

        getData(1)

        return view
    }

    private fun getComponent(view: View){

        new_list=view.findViewById(R.id.new_list)
        new_swipe=view.findViewById(R.id.new_swipe)

    }


    private fun initList(){

        val gridLayoutManager =GridLayoutManager(context,2)
        new_list.adapter=adapter
        new_list.layoutManager=gridLayoutManager
        new_list.addItemDecoration(MainItemSpace())

        new_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!new_list.canScrollVertically(1)){//到底部

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


        adapter.onNewestItemClick=object :OnNewestItemClick{

            override fun clickItem(position: Int, video: Video) {

                startPlayVideo(video)

            }
        }


    }

    private fun initSwipe(){

        new_swipe.setProgressViewOffset(true,20,200)

        new_swipe.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        new_swipe.setOnRefreshListener {

            refresh=true

            p=1//重置p

            vp=p

            getData(p)

        }
    }

    private fun getData(page:Int){

        presenter.getData(page)

    }

    private fun startPlayVideo(video: Video){

        val intent=Intent(context,PlayActivity::class.java)

        intent.putExtra("video",video)

        startActivity(intent)

    }


}
