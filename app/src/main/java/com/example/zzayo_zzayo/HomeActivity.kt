package com.example.zzayo_zzayo

// MainActivity에서 로그인 후 넘어오는 페이지

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var str_id: String
    lateinit var str_name: String

    lateinit var btn_team: Button
    lateinit var btn_create: Button
    lateinit var btn_enter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_team = findViewById(R.id.btn_team)
        btn_create = findViewById(R.id.btn_create)
        btn_enter = findViewById(R.id.btn_enter)

        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        dbManager = DBManager(this, "personnel", null, 1)
        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT name FROM personnel WHERE id = '" + str_id + "';", null)

        while (cursor.moveToNext()) {
            str_name = cursor.getString(cursor.getColumnIndex("name")).toString()
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()


        btn_team.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }
        btn_create.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("intent_name", str_name)
            startActivity(intent)
        }
        btn_enter.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }


    }

    //옵션 메뉴를 액티비티에 표시
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    //옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //메뉴아이템의 ID로 나누는 when문
        when (item?.itemId) {
            R.id.action_logout -> {
                Toast.makeText(this, "로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
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