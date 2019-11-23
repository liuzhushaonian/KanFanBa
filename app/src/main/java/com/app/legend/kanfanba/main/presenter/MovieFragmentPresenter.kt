package com.app.legend.kanfanba.main.presenter

import com.app.legend.ruminasu.presenters.BasePresenter

class MovieFragmentPresenter(fragment: IMovieFragment):BasePresenter<IMovieFragment>() {

    private lateinit var fragment: IMovieFragment

    init {
        attachView(fragment)
        this.fragment=getView()!!
    }


}
