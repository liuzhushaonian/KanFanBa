package com.app.legend.kanfanba.search.presenter

import android.text.Html
import android.util.Log
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.utils.HtmlUtil
import com.app.legend.kanfanba.utils.NetWorkUtil
import com.app.legend.ruminasu.presenters.BasePresenter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SearchPresenter(activity: ISearchActivity):BasePresenter<ISearchActivity>() {

    var activity:ISearchActivity

    init {
        attachView(activity)

        this.activity=getView()!!
    }

    public fun startSearch(key:String){

        Log.d("ss--->>>",key)

        search(key)

    }



    private fun search(key: String){

        Observable.create(ObservableOnSubscribe<String> {

            val url="https://akcp.kanfanba.com/?s=$key"

            val result=NetWorkUtil.getSearchHtml(url)

            if (result.code!=200){

                it.onError(Throwable("msg--->>>${result.code}"))

            }else{

                it.onNext(result.info)
            }

            it.onComplete()

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(

                onNext = {

                    parseEpisodes(it)
                    parseVideos(it)
                },

                onError = {
                    activity.onError(it.message!!)
                }


            )
    }


    /**
     * 解析单个视频文件
     */
    private fun parseVideos(html: String){


        Observable.create(ObservableOnSubscribe<MutableList<Video>> {

            val list=HtmlUtil.parseSearchVideo(html)

            it.onNext(list)

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    activity.setVideoList(it)
                }
            )
    }


    private fun parseEpisodes(html: String){

        Observable.create(ObservableOnSubscribe<MutableList<Episode>> {

            val list=HtmlUtil.parseSearchEpisode(html)
            it.onNext(list)

        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = {
                    activity.setEpisodeList(it)
                }
            )
    }


}