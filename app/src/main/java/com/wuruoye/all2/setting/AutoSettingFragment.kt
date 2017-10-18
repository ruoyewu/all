package com.wuruoye.all2.setting

import android.os.Bundle
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
        switch_setting_auto_main.isChecked = mSettingCache.isAutoMainButton
        switch_setting_auto_detail.isChecked = mSettingCache.isAutoDetailButton

        switch_setting_auto_main.setOnCheckedChangeListener(this)
        switch_setting_auto_detail.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id){
            R.id.switch_setting_auto_main -> {
                mSettingCache.isAutoMainButton = isChecked
            }
            R.id.switch_setting_auto_detail -> {
                mSettingCache.isAutoDetailButton = isChecked
            }
        }
    }
}