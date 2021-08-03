package com.example.zzayo_zzayo

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class WriteActivity : AppCompatActivity() {
    lateinit var registDBManager: RegistDBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edt_title: EditText
    lateinit var edt_content: EditText
    lateinit var btn_regist: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        edt_title = findViewById(R.id.edt_title)
        edt_content = findViewById(R.id.edt_content)
        btn_regist = findViewById(R.id.btn_regist)

        registDBManager = RegistDBManager(this, "registDB", null, 1)

        btn_regist.setOnClickListener {
            var str_title: String = edt_title.text.toString()
            var str_content: String = edt_content.text.toString()

            sqlitedb = registDBManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO regist VALUES ('" + str_title + "', '"
                    + str_content + "')")
            sqlitedb.close()

            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)

        }

    }
}