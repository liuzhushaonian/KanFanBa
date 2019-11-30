package com.app.legend.kanfanba.main.presenter

import android.content.Context
import android.util.Log
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.utils.Conf
import com.app.legend.kanfanba.utils.HtmlUtil
import com.app.legend.kanfanba.utils.NetWorkUtil
import com.app.legend.ruminasu.presenters.BasePresenter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.FormBody
import java.io.File

class NewestFragmentPresenter(fragment: INewestFragment):BasePresenter<INewestFragment>() {

    private lateinit var fragment: INewestFragment

    init {
        attachView(fragment)

        this.fragment=getView()!!
    }

    public fun getData(page: Int){

        getHtml(page)

    }


    /**
     * 获取最新番更新列表的html，然后交给工具类解析并返回解析结果，然后传给Fragment绑定数据
     */
    private fun getHtml(page: Int){

        Observable.create(ObservableOnSubscribe<MutableList<Video>> {

            val html=NetWorkUtil.getHtml(Conf.URL,buildForm(page))

            if (html.code!=200){

                it.onError(Throwable("msg--->>${html.code}"))

            }else {

                val list = HtmlUtil.getNewestList(html.info)

                it.onNext(list)
            }

            it.onComplete()

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(

                onNext = {
                    fragment.setData(it)

                },

                onError = {

                    fragment.onError(it.message!!)

                }
            )
    }

    private fun buildForm(page:Int):FormBody{

        return FormBody.Builder()
            .add("params[block_title]","最近更新")
            .add("params[post_type]","post")
            .add("params[order_by]","date")
            .add("params[order]","DESC")
            .add("params[items_per_page]","24")
            .add("params[post_count]","-1")
            .add("params[data_ajax]","yes")
            .add("params[filter]","98")
            .add("params[tax]","category")
            .add("params[paged]","$page")
            .add("action","blockajaxaction")
            .add("params[post_metas][]","author")
            .add("params[post_metas][]","date-time")
            .add("params[display_categories]","yes")
            .add("params[layout]","grid-sm-fw-col")
            .build()
    }

}