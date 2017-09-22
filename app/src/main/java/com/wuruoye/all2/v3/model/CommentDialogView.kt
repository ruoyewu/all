package com.wuruoye.all2.v3.model

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wuruoye.all2.R
import kotlinx.android.synthetic.main.dialog_article_comment.*

/**
 * Created by wuruoye on 2017/9/22.
 * this file is to do
 */
class CommentDialogView (view: View) {
    val tvUser = view.findViewById<TextView>(R.id.tv_comment_dialog_user)!!
    val tvParent = view.findViewById<TextView>(R.id.tv_comment_dialog_parent)!!
    val et = view.findViewById<EditText>(R.id.et_comment_dialog)!!
    val btnCancel = view.findViewById<Button>(R.id.btn_comment_dialog_cancel)!!
    val btnPublish = view.findViewById<Button>(R.id.btn_comment_dialog_publish)!!
}