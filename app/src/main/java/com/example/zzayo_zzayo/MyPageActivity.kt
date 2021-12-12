package com.example.zzayo_zzayo
// 마이 페이지 옵션을 눌렀을 때
// 아이디와 내가 참여한 팀의 목록을 볼 수 있고, 그 목록을 누르면 상세 페이지를 볼 수 있음
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

class MyPageActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var enterDBManager: EnterDBManager //팀 참여자 정보 DB
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var sqlitedb_enter : SQLiteDatabase //팀 참여자 DB를 위한 SQLite

    lateinit var layout : LinearLayout

    lateinit var tv_id: TextView
    lateinit var str_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        tv_id = findViewById(R.id.tv_id)
        layout = findViewById(R.id.myTeamProject)

        val face = Typeface.createFromAsset(assets, "fonta.ttf")
        val shape = GradientDrawable()
        shape.cornerRadius = 15f
        shape.setColor(Color.parseColor("#50FFAEBC"))

        //로그인 한 사용자의 아이디를 전달받음
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        //출력
        tv_id.text = str_id

        enterDBManager = EnterDBManager(this, "enter", null, 1)
        sqlitedb_enter = enterDBManager.readableDatabase

        var cursor_enter : Cursor
        cursor_enter = sqlitedb_enter.rawQuery("SELECT * FROM enter WHERE userID = '" + str_id + "';",null)

        var num : Int = 0
        while (cursor_enter.moveToNext()) {
            var str_roomName = cursor_enter.getString(cursor_enter.getColumnIndex("roomName")).toString()
            var int_roomPasswd = cursor_enter.getInt(cursor_enter.getColumnIndex("roomPasswd")).toInt()
            var str_roomExplain = cursor_enter.getString(cursor_enter.getColumnIndex("roomExplain")).toString()

            var layout_item : LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id = num
            layout_item.setTag(str_roomName)

            var tv_roomName : TextView = TextView(this)
            tv_roomName.text =  str_roomName
            tv_roomName.textSize = 30F
            tv_roomName.setTextColor(Color.BLACK)
            tv_roomName.setTypeface(face)
            tv_roomName.setBackgroundColor(Color.parseColor("#50FFAEBC"))
            tv_roomName.setBackground(shape)
            layout_item.addView(tv_roomName)

            var tv_roomPasswd : TextView = TextView(this)
            tv_roomPasswd.text = "입장번호: " + int_roomPasswd
            tv_roomPasswd.setTypeface(face)
            tv_roomPasswd.textSize=15f
            layout_item.addView(tv_roomPasswd)

            var tv_roomExplain : TextView = TextView(this)
            tv_roomExplain.text =  str_roomExplain +"\n"
            tv_roomExplain.setTypeface(face)
            tv_roomExplain.textSize=15f
            tv_roomExplain.maxLines=2
            layout_item.addView(tv_roomExplain)

            layout_item.setOnClickListener {
                val intent = Intent(this, RoomInfoActivity::class.java)
                intent.putExtra("intent_roomName", str_roomName)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++
        }

        cursor_enter.close()
        sqlitedb_enter.close()
        enterDBManager.close()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_mypage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.action_logout -> {
                Toast.makeText(this, "로그아웃 성공, 로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
            R.id.action_home -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}