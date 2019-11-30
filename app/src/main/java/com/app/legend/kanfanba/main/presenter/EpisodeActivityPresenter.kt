package com.app.legend.kanfanba.main.presenter

import android.text.Html
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

class EpisodeActivityPresenter(activity: IEpisodeActivity): BasePresenter<IEpisodeActivity>() {

    var activity:IEpisodeActivity

    init {
        attachView(activity)
        this.activity=getView()!!
    }


    public fun getData(episode: Episode){

        getTheData(episode)

    }

    private fun getTheData(episode: Episode){

        Observable.create(ObservableOnSubscribe<MutableList<Video>> {

            val r=NetWorkUtil.getPager(episode.url)

            if (r.code!=200){

                it.onError(Throwable("msg--->>${r.code}"))

            }else {

                val html = r.info

                val list = HtmlUtil.getEpisodeVideos(html)

                it.onNext(list)
            }

            it.onComplete()

        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(

                onNext = {

                    activity.setData(it)

                },

                onError = {

                    activity.onError(it.message!!)

                }

            )

    }



}