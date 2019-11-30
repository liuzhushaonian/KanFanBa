package com.app.legend.kanfanba.main.presenter

import com.app.legend.kanfanba.utils.HtmlUtil
import com.app.legend.kanfanba.utils.NetWorkUtil
import com.app.legend.ruminasu.presenters.BasePresenter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class MainPresenter(activity: IMainActivity) :BasePresenter<IMainActivity>(){

    private lateinit var activity:IMainActivity

    init {
        attachView(activity)
        this.activity=getView()!!
    }


    public fun getSecurity(){

        getTheSecurity()

    }


    private fun getTheSecurity(){

        Observable.create(ObservableOnSubscribe<String> {

            val index=NetWorkUtil.getIndex()

            if (index.code!=200){
                it.onError(Throwable("msg--->>${index.code}"))
                it.onComplete()
            }else {

                val s = HtmlUtil.parseIndex(index.info)

                it.onNext(s)

                it.onComplete()
            }

        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    activity.setSecurityValue(it)
                },

                onError = {

                    activity.onError(it.message!!)

                }
            )
    }



}