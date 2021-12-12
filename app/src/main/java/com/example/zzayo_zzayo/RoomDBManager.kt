package com.example.zzayo_zzayo
// 팀에 대한 카테고리, 팀을 만든 사용자의 아이디, 팀의 이름
// 팀 입장번호, 인원 수, 일정의 시작 날짜와 종료 날짜, 팀에 대한 설명이 존재

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RoomDBManager (
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE room (category text, userID text, roomName text, roomPasswd INTEGER, roomNumber INTEGER, startDate text, endDate text, roomExplain text)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}