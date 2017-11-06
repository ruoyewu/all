package com.wuruoye.all2.setting

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.CompoundButton
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.setting.model.SettingCache
import kotlinx.android.synthetic.main.fragment_auto_setting.*

/**
 * Created by wuruoye on 2017/10/16.
 * this file is to do
 */
class AutoSettingFragment : BaseFragment(), CompoundButton.OnCheckedChangeListener{
    private lateinit var mSettingCache: SettingCache

    override val contentView: Int
        get() = R.layout.fragment_auto_setting

    override fun initData(bundle: Bundle?) {
        mSettingCache = SettingCache(context)
    }

    override fun initView(view: View) {
        switch_setting_auto_main.setOnCheckedChangeListener(this)
        switch_setting_auto_detail.setOnCheckedChangeListener(this)
        switch_setting_change_page.setOnCheckedChangeListener(this)
        switch_setting_black_edge.setOnCheckedChangeListener(this)
        switch_setting_pre_slide.setOnCheckedChangeListener(this)
        switch_setting_auto_image.setOnCheckedChangeListener(this)
        switch_setting_circle_open.setOnCheckedChangeListener(this)

        switch_setting_auto_main.isChecked = mSettingCache.isAutoMainButton
        switch_setting_auto_detail.isChecked = mSettingCache.isAutoDetailButton
        switch_setting_change_page.isChecked = mSettingCache.isSlideBack
        switch_setting_black_edge.isChecked = mSettingCache.isBlackEdge
        switch_setting_pre_slide.isChecked = mSettingCache.isPreSlide
        switch_setting_auto_image.isChecked = mSettingCache.isAutoImage
        switch_setting_circle_open.isChecked = mSettingCache.isCircleOpen

        if (!mSettingCache.isSlideBack){
            val unableColor = ActivityCompat.getColor(context, R.color.mountain_mist)
            tv_setting_black_edge.setTextColor(unableColor)
            tv_setting_pre_slide.setTextColor(unableColor)
            switch_setting_black_edge.isClickable = false
            switch_setting_pre_slide.isClickable = false
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id){
            R.id.switch_setting_auto_main -> {
                mSettingCache.isAutoMainButton = isChecked
            }
            R.id.switch_setting_auto_detail -> {
                mSettingCache.isAutoDetailButton = isChecked
            }
            R.id.switch_setting_auto_image -> {
                mSettingCache.isAutoImage = isChecked
            }
            R.id.switch_setting_change_page -> {
                mSettingCache.isSlideBack = isChecked
                if (!isChecked){
                    val unableColor = ActivityCompat.getColor(context, R.color.mountain_mist)
                    tv_setting_black_edge.setTextColor(unableColor)
                    tv_setting_pre_slide.setTextColor(unableColor)
                    switch_setting_black_edge.isClickable = false
                    switch_setting_pre_slide.isClickable = false
                }else{
                    val enableColor = ActivityCompat.getColor(context, R.color.monsoon)
                    tv_setting_black_edge.setTextColor(enableColor)
                    switch_setting_black_edge.isClickable = true
                    switch_setting_pre_slide.isClickable = true

                    switch_setting_circle_open.isChecked = false
                }
            }
            R.id.switch_setting_black_edge -> {
                mSettingCache.isBlackEdge = isChecked
            }
            R.id.switch_setting_pre_slide -> {
                mSettingCache.isPreSlide = isChecked
            }
            R.id.switch_setting_circle_open -> {
                mSettingCache.isCircleOpen = isChecked
                if (isChecked){
                    switch_setting_change_page.isChecked = false
                }
            }
        }
    }
}