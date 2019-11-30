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
import com.app.legend.kanfanba.utils.CourseSearchAdapterWrapper
import com.app.legend.ruminasu.adapters.BaseAdapter
import com.app.legend.ruminasu.fragments.BasePresenterFragment
import com.app.legend.ruminasu.utils.MainItemSpace
import com.github.ybq.android.spinkit.SpinKitView
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

    private lateinit var spin_kit:SpinKitView

    var courseSearchAdapterWrapper:CourseSearchAdapterWrapper?=null


    override fun setData(list: MutableList<Video>) {

        spin_kit.visibility=View.GONE



        if (list.isNotEmpty()) {

            if (refresh){

                adapter.clean()

                Toast.makeText(context,"已获取最新数据",Toast.LENGTH_SHORT).show()
                refresh=false
                new_swipe.isRefreshing=refresh

//                courseSearchAdapterWrapper?.notifyDataSetChanged()


            }

            adapter.addVideos(list)

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
        spin_kit=view.findViewById(R.id.spin_kit)

    }


    private fun initList(){

        val gridLayoutManager =GridLayoutManager(context,2)
//        new_list.adapter=adapter
        new_list.layoutManager=gridLayoutManager
        new_list.addItemDecoration(MainItemSpace())


        courseSearchAdapterWrapper=CourseSearchAdapterWrapper(context!!,adapter as BaseAdapter<RecyclerView.ViewHolder>,24,new_list)

        courseSearchAdapterWrapper?.setOnLoadMoreListener(object :CourseSearchAdapterWrapper.OnLoadMoreListener{
            override fun onLoadMore() {

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

        new_list.adapter=courseSearchAdapterWrapper


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
