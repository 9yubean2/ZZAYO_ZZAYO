package com.example.zzayo_zzayo
// CreateActivity에서 넘어오는 페이지
// 팀에 대한 정보를 확인할 수 있음
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

    // 팀에 대한 카테고리, 팀을 만든 사용자의 아이디, 팀의 이름
    // 팀 입장번호, 인원 수, 일정의 시작 날짜와 종료 날짜, 팀에 대한 설명이 존재
    lateinit var roomDBManager : RoomDBManager

    lateinit var tv_category : TextView
    lateinit var tv_roomName : TextView
    lateinit var tv_roomPasswd : TextView
    lateinit var tv_roomNumber : TextView
    lateinit var tv_startDate : TextView
    lateinit var tv_endDate : TextView
    lateinit var tv_roomExplain : TextView
    lateinit var btn_OK : Button

    var str_category: String = ""
    lateinit var str_roomName : String
    var int_roomPasswd : Int = 0
    var int_roomNumber : Int = 0
    var str_startDate : String = ""
    var str_endDate : String = ""
    var str_roomExplain : String = ""

    lateinit var str_id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_info)

        tv_category = findViewById(R.id.category)
        tv_roomName = findViewById(R.id.roomName)
        tv_roomPasswd = findViewById(R.id.roomPasswd)
        tv_roomNumber = findViewById(R.id.roomNumber)
        tv_startDate = findViewById(R.id.startDate)
        tv_endDate = findViewById(R.id.endDate)
        tv_roomExplain = findViewById(R.id.roomExplain)
        btn_OK = findViewById(R.id.btn_OK)


        // CreateActivity에서 로그인 한 사용자의 아이디와 팀 이름을 전달
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()
        str_roomName = intent.getStringExtra("intent_roomName").toString()

        roomDBManager = RoomDBManager(this, "room", null, 1)
        sqlitedb = roomDBManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM room WHERE roomName='" + str_roomName + "';", null)

        if (cursor.moveToNext()) {
            str_category= cursor.getString(cursor.getColumnIndex("category")).toString()
            int_roomPasswd = cursor.getInt(cursor.getColumnIndex("roomPasswd"))
            int_roomNumber = cursor.getInt(cursor.getColumnIndex("roomNumber"))
            str_startDate = cursor.getString(cursor.getColumnIndex("startDate")).toString()
            str_endDate = cursor.getString(cursor.getColumnIndex("endDate")).toString()
            str_roomExplain = cursor.getString(cursor.getColumnIndex("roomExplain")).toString()
        }

        cursor.close()
        sqlitedb.close()
        roomDBManager.close()

        tv_category.text = str_category
        tv_roomName.text = str_roomName
        tv_roomPasswd.text = "" + int_roomPasswd
        tv_roomNumber.text = "" + int_roomNumber
        tv_startDate.text = str_startDate
        tv_endDate.text = str_endDate
        tv_roomExplain.text = str_roomExplain

        btn_OK.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            intent.putExtra("intent_id", str_id)
            intent.putExtra("intent_team_title", str_roomName)
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
                intent.putExtra("intent_id", str_id)
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
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}