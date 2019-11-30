package com.app.legend.kanfanba.play.presenter

import com.app.legend.kanfanba.bean.Video
import com.app.legend.kanfanba.utils.HtmlUtil
import com.app.legend.kanfanba.utils.NetWorkUtil
import com.app.legend.ruminasu.presenters.BasePresenter
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.FormBody

class PlayPresenter(activity: IPlayActivity) : BasePresenter<IPlayActivity>() {

    lateinit var activity: IPlayActivity

    init {

        attachView(activity)

        this.activity=activity

    }


    public fun getM3u8(v:Video,security: String){
        parseHtml(v, security)
    }


    private fun parseHtml(v:Video,security: String){

        Observable.create(ObservableOnSubscribe<String> {

            val result=NetWorkUtil.getHtml("https://akcp.kanfanba.com/wp-admin/admin-ajax.php",buildBody(v.post_id,security))

            if (result.code!=200){

                it.onError(Throwable("msg--->>${result.code}"))

            }else {


                val url = HtmlUtil.parseVideoJson(result.info)

                it.onNext(url)
            }
            it.onComplete()


        }).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(

                onNext = {

                    activity.setSource(it)

                },

                onComplete = {


                }


            )


    }


    private fun buildBody(post_id:Int,security:String):FormBody{

        return FormBody.Builder()
            .add("action","get_player_params")
            .add("post_id","$post_id")
            .add("security",security)
            .build()

    }


}