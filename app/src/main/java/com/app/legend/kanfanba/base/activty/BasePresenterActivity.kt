package com.app.legend.ruminasu.activityes

import android.os.Bundle
import com.app.legend.ruminasu.presenters.BasePresenter


abstract class BasePresenterActivity <V,T : BasePresenter<V>> : BaseActivity(){

    protected lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter=createPresenter()
        presenter.attachView(this as V)
    }

    protected abstract fun createPresenter():T

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }


}