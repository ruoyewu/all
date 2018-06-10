package com.wuruoye.all2.user

import android.os.Bundle
import android.view.View
import com.wuruoye.all2.R
import com.wuruoye.all2.base.BaseFragment
import com.wuruoye.all2.base.util.toast
import com.wuruoye.all2.user.model.UserCache
import com.wuruoye.all2.user.presenter.LoginGet
import com.wuruoye.all2.user.presenter.LoginView
import kotlinx.android.synthetic.main.fragment_login_sign.*
import org.json.JSONObject

/**
 * Created by wuruoye on 2017/10/20.
 * this file is to do
 */
class LoginSignFragment : BaseFragment(), View.OnClickListener {
    private lateinit var mUserCache: UserCache
    private lateinit var mLoginGet: LoginGet
    private val mLoginView = object : LoginView{
        override fun onLogin(result: Boolean, info: String) {

        }

        override fun onSign(result: Boolean, info: String) {
            activity?.runOnUiThread {
                if (result){
                    val json = JSONObject(info)
                    val id = json.getInt("id")
                    val name = json.getString("name")
                    mUserCache.signUser(id, name)
                    et_sign_name.setText("")
                    et_sign_pass.setText("")
                    toast("注册成功")
                    mUserCache.isFavoriteChange = true
                    val activity = activity as LoginActivity
                    activity.onSignOk()
                }else {
                    toast(info)
                }
            }
        }

        override fun setWorn(message: String) {
            activity?.runOnUiThread {
                toast(message)
            }
        }

    }

    override val contentView: Int
        get() = R.layout.fragment_login_sign

    override fun initData(bundle: Bundle?) {
        mUserCache = UserCache(context!!)
        mLoginGet = LoginGet()
        mLoginGet.attachView(mLoginView)
    }

    override fun initView(view: View) {
        btn_sign_sign.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.btn_sign_sign -> {
                val name = et_sign_name.text.toString()
                val password = et_sign_pass.text.toString()
                if (name.isEmpty()){
                    til_sign_name.isErrorEnabled = true
                    til_sign_name.error = "用户名不能为空"
                }else if (password.length < 6){
                    til_sign_pass.isErrorEnabled = true
                    til_sign_pass.error = "密码长度应不小于6"
                }else {
                    mLoginGet.signUser(name, password)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginGet.detachView()
    }
}