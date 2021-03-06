package com.example.zzayo_zzayo

// 댓글의 작성자, 댓글을 달려하는 게시글의 제목, 내용이 존재

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CommentDBManager (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) { // 입력한 내용이 commentTBL에 저장
        db!!.execSQL("CREATE TABLE commentTBL (comment_writer text, comment_title text ,comment text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}