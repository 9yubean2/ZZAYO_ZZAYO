package com.example.zzayo_zzayo

// HomeActivity에서 넘어오는 페이지
// 팀 들어가요 (팀 만들어요에서 만든 공간을 비밀번호를 입력한 후 들어가는 용도)

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ListActivity : AppCompatActivity() {

    // 팀에 대한 카테고리, 팀을 만든 사용자의 아이디, 팀의 이름
    // 팀 비밀번호, 인원 수, 일정의 시작 날짜와 종료 날짜, 팀에 대한 설명이 존재
    lateinit var roomdbManager : RoomDBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var layout : LinearLayout

    lateinit var str_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        roomdbManager = RoomDBManager(this, "room", null, 1)
        sqlitedb = roomdbManager.readableDatabase

        layout = findViewById(R.id.teamList)

        val face = Typeface.createFromAsset(assets, "fonta.ttf")
        val shape = GradientDrawable()
        shape.cornerRadius = 20f
        shape.setColor(Color.parseColor("#50FFAEBC"))

        // HomeActivity에서 로그인 한 사용자의 아이디를 전달받음
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM room;", null)

        var num : Int = 0
        var int_count : Int= 0

        while (cursor.moveToNext()) {
            var str_roomName = cursor.getString(cursor.getColumnIndex("roomName")).toString()
            var str_category = cursor.getString(cursor.getColumnIndex("category")).toString()
            var str_roomNumber = cursor.getString(cursor.getColumnIndex("roomNumber")).toString()


            var layout_item : LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id = num

            var tv_roomName : TextView = TextView(this)
            tv_roomName.text =  str_roomName
            tv_roomName.setTypeface(face)
            tv_roomName.textSize = 30f
            tv_roomName.setTextColor(Color.BLACK)
            tv_roomName.setBackgroundColor(Color.parseColor("#50FFAEBC"))
            tv_roomName.setBackground(shape)
            layout_item.addView(tv_roomName)

            var tv_category : TextView = TextView(this)
            tv_category.text = "카테고리 : " + str_category + "\n"
            tv_category.setTypeface(face)
            tv_category.textSize = 20f
            layout_item.addView(tv_category)


            layout_item.setOnClickListener {
                val intent = Intent(this, EnterActivity::class.java)
                intent.putExtra("intent_roomName", str_roomName)
                intent.putExtra("intent_id", str_id)
                intent.putExtra("intent_count", int_count)  //입장 클릭시 int_count++
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++

        }

        cursor.close()
        sqlitedb.close()
        roomdbManager.close()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //메뉴아이템의 ID로 나누는 when문
        when (item?.itemId) {
            R.id.action_home -> {
                var intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                Toast.makeText(this, "로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
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