package com.app.legend.kanfanba.utils

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.legend.kanfanba.R
import com.app.legend.ruminasu.adapters.BaseAdapter

class CourseSearchAdapterWrapper(val context: Context, val mAdapter:BaseAdapter<RecyclerView.ViewHolder>,
                                 val initialPageCount: Int,
                                 val recycler: RecyclerView) : BaseAdapter<RecyclerView.ViewHolder>()  {


    private var mInflater: LayoutInflater? = null
    private var footerType = TYPE_FOOTER_LOADING //默认为正在加载
    private var lastVisibleItem:Int = 0
    private var onLoadMoreListener: OnLoadMoreListener? = null

    var span=2

    init {
        mInflater = LayoutInflater.from(context)
    }

    companion object {
        const val TYPE_FOOTER_LOADING = 100000001 //底部正在加载view
        const val TYPE_FOOTER_NOMORE = 100000002 //底部无更多数据view
    }

    /**
     * 加1是因为包装类为自定义的adapter增加了一个footer
     */
    override fun getItemCount(): Int  = mAdapter.itemCount + 1

    override fun getItemViewType(position: Int): Int {
        //如果首次加载的数据少于 initialPageCount 行，则表明没有更多数据了，直接返回TYPE_FOOTER_NOMORE类型footer

        if (position == mAdapter.itemCount && mAdapter.itemCount < initialPageCount){

            return TYPE_FOOTER_NOMORE


        }
        return when (position < mAdapter.itemCount) {
            // 0-mAdapter.itemCount-1，调用自定义adapter内的getItemViewType
            true -> mAdapter.getItemViewType(position)
            // 第mAdapter.getItemViewType个，是包装类内部定义的，返回footer的ViewType类型值
            false -> footerType
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        var view: View? = null

//        Log.d("p1---->>>","$p1")

        return when (p1) {
            //TYPE_FOOTER_LOADING是加载更多footer的ViewType类型值
            TYPE_FOOTER_LOADING -> {

                val gridLayoutManager:GridLayoutManager= recycler.layoutManager as GridLayoutManager

                gridLayoutManager.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {

                        if (getItemViewType(position)== TYPE_FOOTER_LOADING) {
                            return span
                        }

                        return 1

                    }


                }

                val view=LayoutInflater.from(p0.context).inflate(R.layout.footer,p0,false)


                if (mAdapter.itemCount==0){

                    view.visibility=View.GONE

                }

                FooterHolder(view, TYPE_FOOTER_LOADING)
            }
            //TYPE_FOOTER_NOMORE是到达底部footer的ViewType类型值
            TYPE_FOOTER_NOMORE -> {

                val gridLayoutManager:GridLayoutManager= recycler.layoutManager as GridLayoutManager

                gridLayoutManager.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {

                        if (getItemViewType(position)== TYPE_FOOTER_NOMORE) {
                            return span
                        }

                        return 1

                    }


                }

                val view=LayoutInflater.from(p0.context).inflate(R.layout.footer,p0,false)

                if (mAdapter.itemCount==0){

                    view.visibility=View.GONE

                }

                FooterHolder(view, TYPE_FOOTER_NOMORE)
            }
            //其余情况均返回自定义adapter内部的onCreateViewHolder
            else -> {
                mAdapter.onCreateViewHolder(p0, p1)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            //如果是footer，那么自己处理，不然就调用自定义adapter内部的onBindViewHolder
            is FooterHolder -> {

                if (footerType== TYPE_FOOTER_NOMORE){

                    holder.itemView.visibility=View.VISIBLE
                    holder.txt?.text="到底了,不如看番吧"

                }

            }
            else -> {
                mAdapter.onBindViewHolder(holder, position)
            }
        }
    }

    //需要加载更多数据时调用
    public fun insertData() {
        footerType = TYPE_FOOTER_LOADING
        notifyDataSetChanged()
    }

    //没有更多数据时候调用
    public fun noMoreData() {
        footerType = TYPE_FOOTER_NOMORE

        val gridLayoutManager:GridLayoutManager= recycler.layoutManager as GridLayoutManager

        gridLayoutManager.spanSizeLookup=object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                if (getItemViewType(position)== TYPE_FOOTER_NOMORE) {
                    return span
                }

                return 1

            }


        }

        notifyItemChanged(mAdapter.itemCount)
    }

    fun setOnScrollerListener() {

        recycler.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager

                when (layoutManager) {
                    is LinearLayoutManager -> {
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    }
                    //这里现在只写了LinearLayoutManager的代码，后续补充
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE //停止滚动
                    && footerType == TYPE_FOOTER_LOADING  //footer状态为加载更多
                    && lastVisibleItem >= mAdapter.itemCount)  //最后一个显示的item序号大于适配器item数量，即显示了footer
                    onLoadMoreListener?.onLoadMore()
            }
        })
    }

    fun setOnLoadMoreListener (listener: OnLoadMoreListener): CourseSearchAdapterWrapper {
        this.onLoadMoreListener = listener
        //设置滑动监听
        setOnScrollerListener()
        return this
    }

    interface OnLoadMoreListener {
        fun onLoadMore() //加载更多
    }

    /**
     * 自定义的底部item，后续会继续暴露方法给外部动态设置加载更多时候的gif图
     */
    class FooterHolder(view: View, type:Int): RecyclerView.ViewHolder(view) {
        var txt: TextView? = null

        init {
            txt = view.findViewById(R.id.footer_txt)
            when (type) {
                TYPE_FOOTER_LOADING -> {
                    txt!!.text = "正在加载……"
                }
                TYPE_FOOTER_NOMORE -> {
                    txt!!.text = "已经到达底部"
                }
            }
        }
    }





}