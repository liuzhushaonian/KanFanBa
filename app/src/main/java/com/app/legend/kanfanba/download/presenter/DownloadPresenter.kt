package com.app.legend.kanfanba.download.presenter

import com.app.legend.ruminasu.presenters.BasePresenter

class DownloadPresenter(activity: IDownloadActivity):BasePresenter<IDownloadActivity>() {


    var activity: IDownloadActivity

    init {
        attachView(activity)
        this.activity=getView()!!
    }



}