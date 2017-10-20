package com.wuruoye.all2.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.transitionseverywhere.TransitionManager
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.base.model.Config
import com.wuruoye.all2.base.model.Listener
import com.wuruoye.all2.base.util.NetUtil
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.user.presenter.LoginGet
import com.wuruoye.all2.user.presenter.LoginView
import kotlinx.android.synthetic.main.fragment_login_login.*
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/10/20.
 * this file is to do
 */
class LoginLoginFragment : BaseFragment(), View.OnClickListener {

    private lateinit var mUserCache: UserCache
    private lateinit var mLoginGet: LoginGet
    private val mLoginView = object : LoginView {
        override fun onLogin(result: Boolean, info: String) {
            activity.runOnUiThread {
                if (result){
                    val json = JSONObject(info)
                    val id = json.getInt("id")
                    val name = json.getString("name")
                    onLogin(id, name)
                }else{
                    tv_login_worn.text = info
                }
            }
        }

        override fun onSign(result: Boolean, info: String) {

        }

        override fun setWorn(message: String) {
            activity.runOnUiThread{
                toast(message)
            }
        }

    }

    override val contentView: Int
        get() = R.layout.fragment_login_login

    override fun initData(bundle: Bundle?) {
        mUserCache = UserCache(context)
        mLoginGet = LoginGet()
        mLoginGet.attachView(mLoginView)
    }

    override fun initView(view: View) {
        if (mUserCache.isLogin){
            changeType(TYPE_LOGIN)
        }else {
            changeType(TYPE_NO_LOGIN)
        }

        btn_login_login.setOnClickListener(this)
        btn_login_out.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.btn_login_login -> {
                val name = et_login_name.text.toString()
                val password = et_login_pass.text.toString()
                mLoginGet.loginUser(name, password)
            }
            R.id.btn_login_out -> {
                mUserCache.cancelUser()
                changeType(TYPE_NO_LOGIN)
            }
        }
    }

    private fun onLogin(id: Int, name: String){
        mUserCache.loginUser(id, name)
        changeType(TYPE_LOGIN)
        NetUtil.downloadFile(Config.USER_AVATAR_URL + "/" + id, "avatar.jpg", object : Listener<String> {
            override fun onSuccess(model: String) {

            }

            override fun onFail(message: String) {

            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun changeType(type: Int){
        TransitionManager.beginDelayedTransition(ll_login)
        tv_login_worn.text = ""
        when (type){
            TYPE_LOGIN -> {
                tv_login_user.visibility = View.VISIBLE
                btn_login_out.visibility = View.VISIBLE

                til_login_name.visibility = View.GONE
                til_login_pass.visibility = View.GONE
                btn_login_login.visibility = View.GONE

                tv_login_user.text = mUserCache.userName + "\t已登录"
            }
            TYPE_NO_LOGIN -> {
                til_login_name.visibility = View.VISIBLE
                til_login_pass.visibility = View.VISIBLE
                btn_login_login.visibility = View.VISIBLE

                tv_login_user.visibility = View.GONE
                btn_login_out.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginGet.detachView()
    }

    companion object {
        val TYPE_LOGIN = 1
        val TYPE_NO_LOGIN = 2
    }

}