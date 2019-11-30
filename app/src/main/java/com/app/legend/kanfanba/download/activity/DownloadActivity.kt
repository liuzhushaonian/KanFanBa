package com.app.legend.kanfanba.download.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.download.presenter.DownloadPresenter
import com.app.legend.kanfanba.download.presenter.IDownloadActivity
import com.app.legend.ruminasu.activityes.BasePresenterActivity

class DownloadActivity : BasePresenterActivity<IDownloadActivity,DownloadPresenter>(),IDownloadActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
    }

    override fun createPresenter(): DownloadPresenter {
        return DownloadPresenter(this)
    }



}
