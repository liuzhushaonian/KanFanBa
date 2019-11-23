package com.app.legend.kanfanba.main.presenter

import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.bean.Video
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


        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(



            )


    }



}