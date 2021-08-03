package com.example.zzayo_zzayo
//마이페이지 메뉴 아이템 눌렀을 때 페이지
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MyPageActivity : AppCompatActivity() {
    lateinit var dbManager: DBManager //사용자 정보 DB
    lateinit var roomdbManager: RoomDBManager //방 정보 DB
    lateinit var sqlitedb : SQLiteDatabase //사용자 정보 DB를 위한 SQLite
    lateinit var sqlitedb_room : SQLiteDatabase //방 정보 DB를 위한 SQLite
    lateinit var layout : LinearLayout

    lateinit var tv_id: TextView
    lateinit var tv_name: TextView
    lateinit var str_name: String
    lateinit var str_id: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        tv_id = findViewById(R.id.tv_id)
        tv_name = findViewById(R.id.tv_name)
        layout = findViewById(R.id.myTeamProject)

        // MainActivity -> HomeActivity에서 로그인 된 이름을 전달받음
        val intent = intent
        str_name = intent.getStringExtra("intent_name").toString()

        // 사용자 정보 DB 읽어오기
        dbManager = DBManager(this, "personnel", null, 1)
        sqlitedb = dbManager.readableDatabase

        //str_name에 해당하는 id 가져오기
        var cursor_personnel : Cursor
        cursor_personnel = sqlitedb.rawQuery("SELECT id FROM personnel WHERE name = '" + str_name + "';", null)

        //출력
        while (cursor_personnel.moveToNext()) {
            str_id = cursor_personnel.getString(cursor_personnel.getColumnIndex("id")).toString()

            tv_id.text = str_id
            tv_name.text = str_name
        }

        cursor_personnel.close()
        sqlitedb.close()
        dbManager.close()

        var cursor_room : Cursor
        cursor_room = sqlitedb.rawQuery("SELECT * FROM room WHERE userName = '" + str_name + "';",null)

        var num : Int = 0
        while (cursor_room.moveToNext()) {
            var str_roomName = cursor_room.getString(cursor_room.getColumnIndex("name")).toString()
            var str_roomExplain = cursor_room.getString(cursor_room.getColumnIndex("roomExplain")).toString()

            var layout_item : LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id = num
            layout_item.setTag(str_roomName)

            var tv_roomName : TextView = TextView(this)
            tv_roomName.text = "방 이름 : " + str_roomName
            tv_roomName.textSize = 30F
            tv_roomName.setBackgroundColor(Color.GRAY)
            layout_item.addView(tv_roomName)

            var tv_roomExplain : TextView = TextView(this)
            tv_roomExplain.text = "방 소개 : " + str_roomExplain
            layout_item.addView(tv_roomExplain)

            layout_item.setOnClickListener {
                val intent = Intent(this, RoomInfoActivity::class.java)
                intent.putExtra("intent_roomName", str_roomName)
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++
        }

        cursor_room.close()
        sqlitedb_room.close()
        roomdbManager.close()
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
                intent.putExtra("intent_name", str_name)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}