package com.wuruoye.all2.user.model

import android.support.design.widget.AppBarLayout

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {

    enum class State{
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var mCurrentState = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (verticalOffset == 0){
            if (mCurrentState != State.EXPANDED){
                onStateChanged(appBarLayout, State.EXPANDED)
            }
            mCurrentState = State.EXPANDED
        }else if (Math.abs(verticalOffset) >= appBarLayout!!.totalScrollRange){
            if (mCurrentState != State.COLLAPSED){
                onStateChanged(appBarLayout, State.COLLAPSED)
            }
            mCurrentState = State.COLLAPSED
        }else{
            if (mCurrentState != State.IDLE){
                onStateChanged(appBarLayout, State.IDLE)
            }
            mCurrentState = State.IDLE
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout?, state: State)
}