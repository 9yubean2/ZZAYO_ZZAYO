package com.example.zzayo_zzayo

// ListActivity에서 넘어오는 페이지
// 팀 만들어요에서 만든 공간을 입장번호를 입력한 후 입장

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*

class EnterActivity : AppCompatActivity() {

    // 팀에 대한 카테고리, 팀을 만든 사용자의 아이디, 팀의 이름
    // 팀 입장번호, 인원 수, 일정의 시작 날짜와 종료 날짜, 팀에 대한 설명이 존재
    lateinit var roomDBManager : RoomDBManager
    lateinit var enterdbManager : EnterDBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var tv_category: TextView
    lateinit var tv_roomName: TextView
    lateinit var tv_roomNumber: TextView
    lateinit var tv_startDate: TextView
    lateinit var tv_endDate: TextView
    lateinit var tv_roomExplain: TextView
    lateinit var edt_roomPasswd: EditText
    lateinit var btn_enter: Button

    lateinit var str_id: String

    var str_category: String = ""
    var str_roomName : String = ""
    var int_roomNumber : Int = 0
    var str_startDate : String = ""
    var str_endDate : String = ""
    var str_roomExplain : String = ""
    var int_roomPasswd : Int = 0

    var imm : InputMethodManager ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        tv_category = findViewById(R.id.category)
        tv_roomName = findViewById(R.id.roomName)
        tv_roomNumber = findViewById(R.id.roomNumber)
        tv_startDate = findViewById(R.id.startDate)
        tv_endDate = findViewById(R.id.endDate)
        tv_roomExplain = findViewById(R.id.roomExplain)
        btn_enter = findViewById(R.id.btn_enter)
        edt_roomPasswd = findViewById(R.id.roomPasswd)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        // ListActivity에서 사용자의 아이디와 팀의 이름을 전달 받음
        val intent = intent
        str_roomName = intent.getStringExtra("intent_roomName").toString()
        str_id = intent.getStringExtra("intent_id").toString()
//        var int_count = intent.getIntExtra("intent_count").toInt()

        // room에서 id 관련된 정보 가져오기
        roomDBManager = RoomDBManager(this, "room", null, 1)
        sqlitedb = roomDBManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM room WHERE roomName='" + str_roomName + "';",null)

        while (cursor.moveToNext()) {
            str_category= cursor.getString(cursor.getColumnIndex("category")).toString()
//            str_roomName = cursor.getString(cursor.getColumnIndex("roomName")).toString()
            int_roomNumber = cursor.getInt(cursor.getColumnIndex("roomNumber"))
            str_startDate = cursor.getString(cursor.getColumnIndex("startDate")).toString()
            str_endDate = cursor.getString(cursor.getColumnIndex("endDate")).toString()
            str_roomExplain = cursor.getString(cursor.getColumnIndex("roomExplain")).toString()
            int_roomPasswd = cursor.getInt(cursor.getColumnIndex("roomPasswd"))
        }

        cursor.close()
        sqlitedb.close()
        roomDBManager.close()

        // 출력하는 곳
        tv_category.text = str_category
        tv_roomName.text = str_roomName
        tv_roomNumber.text = "" + int_roomNumber
        tv_startDate.text = str_startDate
        tv_endDate.text = str_endDate
        tv_roomExplain.text = str_roomExplain

        // 입장 버튼을 눌렀을 때
        btn_enter.setOnClickListener {

            //팀에 이미 참여한 사람이 입장하지 못하게 하기
            enterdbManager = EnterDBManager(this, "enter", null, 1)
            var sqlitedb_read = enterdbManager.readableDatabase
            var cursor : Cursor
            cursor = sqlitedb_read.rawQuery("SELECT userID FROM enter WHERE roomName= '" + str_roomName + "';", null)

            var id_check = 0
            while (cursor.moveToNext()) {
                var str_id_enter = cursor.getString(cursor.getColumnIndex("userID")).toString()

                if (str_id == str_id_enter) {  // 현재 사용자가 이미 해당 팀에 속해 있는 경우
                    id_check = 1
                }
            }
            if (id_check == 1) {
                Toast.makeText(this, "이미 참여한 방입니다" + "\n" + "입장하시려면 마이페이지로 이동해주세요", Toast.LENGTH_SHORT).show()
            } else {

                var str_roomPasswd: String = edt_roomPasswd.text.toString()

                //비밀번호 체크
                if (str_roomPasswd.equals(int_roomPasswd.toString())) {

                    sqlitedb = enterdbManager.writableDatabase

                    sqlitedb.execSQL("INSERT INTO enter VALUES ('" + str_id + "','" + str_roomName
                            + "','" + int_roomPasswd + "','" + str_roomExplain +"');")


                    sqlitedb.close()
                    enterdbManager.close()

                    Toast.makeText(this, "입장하셨습니다", Toast.LENGTH_SHORT).show()

                    //입장한 팀을 마이페이지에서 확인인
                   val intent = Intent(this, MyPageActivity::class.java)
                    intent.putExtra("intent_id", str_id)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "입장 실패, 다시 입력 해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            cursor.close()
            sqlitedb_read.close()
            enterdbManager.close()
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
    //이벤트 메서드 생성
    //Activity 최상위 Layout에 onClick 세팅 : 해당 레이아웃 안에서 view 클릭하면 키보드가 내려가게 함
    fun hide(v: View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }
}