package com.wuruoye.all2.base.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.wuruoye.all2.v3.model.bean.ArticleListItem

/**
 * Created by wuruoye on 2017/11/5.
 * this file is to do
 */

object SQLiteUtil {
    private val READ_DATABASE = 1
    private val WRITE_DATABASE = 2

    private val ADD_ARTICLE = "insert into ${SQLHelper.ARTICLE_TABLE} (userid, content) values (?, ?)"
    private val QUERY_ARTICLE = "select * from ${SQLHelper.ARTICLE_TABLE} where userid = ? order by id desc"
    private val QUERY_ARTICLE_CONTENT = "select * from ${SQLHelper.ARTICLE_TABLE} where userid = ? and content = ?"
    private val DELETE_ARTICLE_ID = "delete from ${SQLHelper.ARTICLE_TABLE} where id = ?"

    fun addArticle(context: Context, item: ArticleListItem, userId: Int){
        val db = openDB(context, WRITE_DATABASE)
        val content = Gson().toJson(item)

        val cur = db.rawQuery(QUERY_ARTICLE_CONTENT, arrayOf(userId.toString(), content))
        if (cur.moveToFirst()){
            val id = cur.getInt(cur.getColumnIndex("id"))
            db.execSQL(DELETE_ARTICLE_ID, arrayOf(id))
        }

        db.execSQL(ADD_ARTICLE, arrayOf(userId, content))
        cur.close()
        db.close()
    }

    fun queryArticle(context: Context, userId: Int): ArrayList<ArticleListItem>{
        val articleList = ArrayList<ArticleListItem>()
        val db = openDB(context, READ_DATABASE)
        val cur = db.rawQuery(QUERY_ARTICLE, arrayOf(userId.toString()))
        if (cur.moveToFirst()){
            do {
                val content = cur.getString(cur.getColumnIndex("content"))
                articleList.add(Gson().fromJson(content, ArticleListItem::class.java))
            }while (cur.moveToNext())
        }
        cur.close()
        db.close()
        return articleList
    }

    private fun openDB(context: Context, openType: Int): SQLiteDatabase{
        val helper = SQLHelper(context)
        return if (READ_DATABASE == openType){
                helper.readableDatabase
            }else {
                helper.writableDatabase
            }
    }
}
