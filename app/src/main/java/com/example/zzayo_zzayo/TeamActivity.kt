package com.example.zzayo_zzayo

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TeamActivity : AppCompatActivity() {

    lateinit var registDBManager: RegistDBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var list_regist : LinearLayout

    lateinit var btn_write: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        btn_write = findViewById(R.id.btn_write)

        registDBManager = RegistDBManager(this,"registDB",null,1)
        sqlitedb = registDBManager.readableDatabase

        list_regist = findViewById(R.id.list_regist)

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM regist",null)

        var num : Int =0
        while(cursor.moveToNext()){
            var str_title = cursor.getString(cursor.getColumnIndex("title")).toString()
            var str_content = cursor.getString(cursor.getColumnIndex("content")).toString()

            var list_regist_item: LinearLayout = LinearLayout(this)
            list_regist_item.orientation = LinearLayout.VERTICAL
            list_regist_item.id = num

            var tv_title : TextView = TextView(this)
            tv_title.text = str_title
            tv_title.textSize = 30f
            tv_title.setBackgroundColor(Color.LTGRAY)
            list_regist_item.addView(tv_title)

            var tv_content : TextView = TextView(this)
            tv_content.text = str_content
            list_regist_item.addView(tv_content)

            //게시글 제목
            list_regist_item.setOnClickListener {
                val intent = Intent(this,ReadActivity::class.java)
                intent.putExtra("intent_title",str_title)
                startActivity(intent)
            }
            list_regist.addView(list_regist_item)
            num++
        }
        cursor.close()
        sqlitedb.close()
        registDBManager.close()

        btn_write.setOnClickListener {
            val intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
        }

    }
}