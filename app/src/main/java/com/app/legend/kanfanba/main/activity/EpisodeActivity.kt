package com.app.legend.kanfanba.main.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.legend.kanfanba.R
import com.app.legend.kanfanba.bean.Episode
import com.app.legend.kanfanba.main.presenter.EpisodeActivityPresenter
import com.app.legend.kanfanba.main.presenter.IEpisodeActivity
import com.app.legend.ruminasu.activityes.BasePresenterActivity
import kotlinx.android.synthetic.main.activity_episode.*

class EpisodeActivity : BasePresenterActivity<IEpisodeActivity,EpisodeActivityPresenter>(),IEpisodeActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)

        initToolbar()

    }

    override fun createPresenter(): EpisodeActivityPresenter {
        return EpisodeActivityPresenter(this)
    }

    private fun initToolbar(){

        ep_toolbar.title=""

        setSupportActionBar(ep_toolbar)

        ep_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)

        ep_toolbar.setNavigationOnClickListener {

            finish()
        }

    }

    private fun initData(){

        val episode=intent.getParcelableExtra<Episode>("episode")

        if (episode!=null){




        }

    }


}
