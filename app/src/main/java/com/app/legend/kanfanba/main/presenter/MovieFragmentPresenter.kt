package com.app.legend.kanfanba.main.presenter

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

class MovieFragmentPresenter(fragment: IMovieFragment):BasePresenter<IMovieFragment>() {

    private lateinit var fragment: IMovieFragment

    init {
        attachView(fragment)
        this.fragment=getView()!!
    }

    public fun getData(page:Int){


        getTheData(page)
    }

    private fun getTheData(page: Int){


        Observable.create(ObservableOnSubscribe<MutableList<Video>> {

            val html=NetWorkUtil.getHtml(Conf.URL,buildForm(page))

            if (html.code!=200){

                it.onError(Throwable("msg--->>${html.code}"))
                it.onComplete()

            }else {

                val list = HtmlUtil.getMovies(html.info)

                it.onNext(list)
            }


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

    private fun buildForm(page:Int): FormBody {

        return FormBody.Builder()
            .add("params[block_title]","")
            .add("params[layout]","grid-mv-fw-col")
            .add("params[post_type]","post-video")
            .add("params[filter_items]","2movie,3movie")
            .add("params[category]","2movie,3movie")
            .add("params[order_by]","view")
            .add("params[order]","DESC")
            .add("params[items_per_page]","24")
            .add("params[post_count]","-1")
            .add("params[display_categories]","no")
            .add("params[display_excerpt]","on")
            .add("params[post_metas][]","author")
            .add("params[post_metas][]","date-time")
            .add("params[post_metas][]","comment-count")
            .add("params[post_metas][]","view-count")
            .add("params[link_to]","default")
            .add("params[sub_class]","movie-grid")
            .add("params[rnd_id]","vp_73462")
            .add("params[data_ajax]","yes")
            .add("params[paged]","$page")
            .add("action","blockajaxaction")
            .build()

    }


}
