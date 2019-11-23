package com.app.legend.kanfanba.main.presenter

import android.util.Log
import com.app.legend.kanfanba.bean.Episode
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

class EpisodeFragmentPresenter(fragment:IEpisodeFragment):BasePresenter<IEpisodeFragment>() {

    private lateinit var fragment: IEpisodeFragment

    init {
        attachView(fragment)
        this.fragment=getView()!!
    }


    public fun  getData(p:Int){

        getTheData(p)
    }

    private fun getTheData(page: Int){


        Observable.create(ObservableOnSubscribe<MutableList<Episode>> {

            val html=NetWorkUtil.getHtml(Conf.URL,buildForm(page))

            val list=HtmlUtil.getEpisodeList(html!!)

            it.onNext(list)

            it.onComplete()


        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(

                onNext = {

                    fragment.setData(it)
                },

                onComplete = {



                }

            )

    }




    private fun buildForm(page:Int): FormBody {

        return FormBody.Builder()
            .add("params[block_title]","剧集节目")
            .add("params[layout]","grid-sm-fw-col")
            .add("params[post_type]","vid_playlist")
            .add("params[filter_items]","dmjm_playlist,rjjm_playlist")
            .add("params[category]","dmjm_playlist,rjjm_playlist")
            .add("params[order_by]","date")
            .add("params[order]","DESC")
            .add("params[items_per_page]","24")
            .add("params[post_count]","-1")
            .add("params[display_categories]","no")
            .add("params[post_metas][]","author")
            .add("params[post_metas][]","date-time")
            .add("params[sub_class]","grid-small")
            .add("params[rnd_id]","vp_80252")
            .add("params[data_ajax]","yes")
            .add("params[filter]","101")
            .add("params[tax]","category")
            .add("params[paged]","$page")
            .add("action","blockajaxaction")
            .build()

    }

}