package com.example.zzayo_zzayo

// 회원가입을 한 사용자의 이름, 아이디, 비밀번호가 존재

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) { // 입력한 내용이 personnel에 저장
        db!!.execSQL("CREATE TABLE personnel (name text, id text, passwd text)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}