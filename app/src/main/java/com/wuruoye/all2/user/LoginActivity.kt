package com.wuruoye.all2.user

import android.os.Bundle
import android.view.View
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseActivity
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.user.presenter.LoginGet
import com.wuruoye.all2.user.presenter.LoginView
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by wuruoye on 2017/9/24.
 * this file is to do
 */
class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserCache: UserCache
    private lateinit var mLoginGet: LoginGet
    private val mLoginView = object : LoginView{
        override fun onLogin(result: Boolean, info: String) {
            runOnUiThread {
                if (result){
                    mUserCache.loginUser(info)
                    NetUtil.downloadFile(Config.USER_AVATAR_URL + "/" + info, "avatar.jpg", object : Listener<String>{
                        override fun onSuccess(model: String) {

                        }

                        override fun onFail(message: String) {

                        }

                    })
                    changeMode()
                }else{
                    tv_login_worn.text = info
                }
            }
        }

        override fun onSign(result: Boolean, info: String) {
            runOnUiThread {
                if (result){
                    mUserCache.signUser(info)
                    changeMode()
                }else{
                    tv_login_worn.text = info
                }
            }
        }

        override fun setWorn(message: String) {
            runOnUiThread {
                toast(message)
            }
        }

    }

    override val contentView: Int
        get() = R.layout.activity_login

    override fun initData(bundle: Bundle?) {
        mUserCache = UserCache(applicationContext)
        mLoginGet = LoginGet()
        mLoginGet.attachView(mLoginView)
    }

    override fun initView() {
        changeMode()

        btn_login_login.setOnClickListener(this)
        btn_login_sign.setOnClickListener(this)
        btn_login_out.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id){
            R.id.btn_login_login -> {
                val name = et_login_name.text.toString()
                val password = et_login_pass.text.toString()
                mLoginGet.loginUser(name, password)
            }
            R.id.btn_login_sign -> {
                val name = et_login_name.text.toString()
                val password = et_login_pass.text.toString()
                mLoginGet.signUser(name, password)
            }
            R.id.btn_login_out -> {
                mUserCache.isLogin = false
                mUserCache.userName = ""
                changeMode()
            }
        }
    }

    private fun changeMode(){
        TransitionManager.beginDelayedTransition(ll_login)
        if (mUserCache.isLogin){
            tv_login_user.visibility = View.VISIBLE
            btn_login_out.visibility = View.VISIBLE

            til_login_name.visibility = View.GONE
            til_login_pass.visibility = View.GONE
            btn_login_login.visibility = View.GONE
            btn_login_sign.visibility = View.GONE
            tv_login_worn.visibility = View.GONE

            tv_login_user.text = mUserCache.userName + "\t" + "已登录"

            et_login_name.setText("")
            et_login_pass.setText("")
            tv_login_worn.text = ""
        }else{
            til_login_name.visibility = View.VISIBLE
            til_login_pass.visibility = View.VISIBLE
            btn_login_login.visibility = View.VISIBLE
            btn_login_sign.visibility = View.VISIBLE
            tv_login_worn.visibility = View.VISIBLE

            tv_login_user.visibility = View.GONE
            btn_login_out.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginGet.detachView()
    }

    companion object {

    }
}