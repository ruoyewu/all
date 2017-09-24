package com.wuruoye.all2.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.wuruoye.all2.R

import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.util.BlurUtil
import kotlinx.android.synthetic.main.activity_user.*

/**
 * Created by wuruoye on 2017/9/23.
 * this file is to do
 */

class UserActivity : BaseActivity() {


    override val contentView: Int
        get() = R.layout.activity_user

    override fun initData(bundle: Bundle?) {

    }

    override fun initView() {
        iv_user_head.setImageBitmap(
                BlurUtil.blurBitmap(applicationContext, BitmapFactory.decodeResource(resources, R.drawable.ic_avatar), 5f)
        )
    }
}
