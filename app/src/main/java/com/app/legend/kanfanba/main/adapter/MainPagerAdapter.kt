package com.app.legend.kanfanba.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.legend.kanfanba.main.fragment.EpisodeFragment
import com.app.legend.kanfanba.main.fragment.MovieFragment
import com.app.legend.kanfanba.main.fragment.NewestFragment

class MainPagerAdapter(fm: FragmentManager, private val list: List<String>):FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {

        when(position){

            0->{
                return NewestFragment()
            }

            1->{
                return EpisodeFragment()
            }

            2->{
                return MovieFragment()
            }
        }

        return Fragment()

    }

    override fun getCount(): Int {
        return list.size
    }
}