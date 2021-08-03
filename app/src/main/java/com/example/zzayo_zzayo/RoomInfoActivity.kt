package com.example.zzayo_zzayo

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class RoomInfoActivity : AppCompatActivity() {

    lateinit var sqlitedb : SQLiteDatabase
    lateinit var roomDBManager : RoomDBManager

    lateinit var tv_roomName : TextView
    lateinit var tv_roomPasswd : TextView
    lateinit var tv_roomNumber : TextView
    lateinit var tv_teamNumber : TextView
    lateinit var tv_teamOrganizationMethod : TextView
    lateinit var tv_startDate : TextView
    lateinit var tv_endDate : TextView
    lateinit var tv_roomExplain : TextView
    lateinit var btn_OK : Button

    lateinit var str_roomName : String
    var int_roomPasswd : Int = 0
    var int_roomNumber : Int = 0
    var int_teamNumber : Int = 0
    var str_teamOrganizationMethod : String = ""
    var str_startDate : String = ""
    var str_endDate : String = ""
    var str_roomExplain : String = ""

    lateinit var str_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_info)

        tv_roomName = findViewById(R.id.roomName)
        tv_roomPasswd = findViewById(R.id.roomPasswd)
        tv_roomNumber = findViewById(R.id.roomNumber)
        tv_teamNumber = findViewById(R.id.teamNumber)
        tv_teamOrganizationMethod = findViewById(R.id.teamOrganizationMethod)
        tv_startDate = findViewById(R.id.startDate)
        tv_endDate = findViewById(R.id.endDate)
        tv_roomExplain = findViewById(R.id.roomExplain)
        btn_OK = findViewById(R.id.btn_OK)


        //HomeActivity에서 사용자 이름 받아오기
        val intent = intent
        str_name = intent.getStringExtra("intent_name").toString()

        //CreateActivity에서 방 이름 받아오기
        val intent_room = intent
        str_roomName = intent.getStringExtra("intent_roomName").toString()

        roomDBManager = RoomDBManager(this, "room", null, 1)
        sqlitedb = roomDBManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM room WHERE name='" + str_roomName + "';", null)

        if (cursor.moveToNext()) {
            int_roomPasswd = cursor.getInt(cursor.getColumnIndex("passwd"))
            int_roomNumber = cursor.getInt(cursor.getColumnIndex("roomNumber"))
            int_teamNumber = cursor.getInt(cursor.getColumnIndex("teamNumber"))
            str_teamOrganizationMethod= cursor.getString(cursor.getColumnIndex("organizationMethod")).toString()
            str_startDate = cursor.getString(cursor.getColumnIndex("startDate")).toString()
            str_endDate = cursor.getString(cursor.getColumnIndex("endDate")).toString()
            str_roomExplain = cursor.getString(cursor.getColumnIndex("roomExplain")).toString()
        }

        cursor.close()
        sqlitedb.close()
        roomDBManager.close()

        tv_roomName.text = str_roomName
        tv_roomPasswd.text = "" + int_roomPasswd
        tv_roomNumber.text = "" + int_roomNumber
        tv_teamNumber.text = "" + int_teamNumber
        tv_teamOrganizationMethod.text = str_teamOrganizationMethod
        tv_startDate.text = str_startDate
        tv_endDate.text = str_endDate
        tv_roomExplain.text = str_roomExplain

        btn_OK.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // 옵션 메뉴를 액티비티에 표시
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // 옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 메뉴 아이템의 id로 나누는 when문
        when(item?.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("intent_name", str_name)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                Toast.makeText(this, "로그아웃 성공, 로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
            R.id.action_mypage -> {
                val intent = Intent(this, MyPageActivity::class.java)
                intent.putExtra("intent_name", str_name)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}