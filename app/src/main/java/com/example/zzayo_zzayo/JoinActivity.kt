package com.example.zzayo_zzayo

// 회원가입 페이지
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText

class JoinActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edt_join_name: EditText
    lateinit var edt_join_id: EditText
    lateinit var edt_join_passwd: EditText

    lateinit var btn_join: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        edt_join_name = findViewById(R.id.edt_join_name)
        edt_join_id = findViewById(R.id.edt_join_id)
        edt_join_passwd = findViewById(R.id.edt_join_passwd)

        btn_join = findViewById(R.id.btn_join)

        dbManager = DBManager(this, "personnel", null, 1)

        btn_join.setOnClickListener {
            var str_join_name: String = edt_join_name.text.toString()
            var str_join_id: String = edt_join_id.text.toString()
            var str_join_passwd: String = edt_join_passwd.text.toString()

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO personnel VALUES ('" + str_join_name + "', '" + str_join_id + "', '" + str_join_passwd + "');")
            sqlitedb.close()

            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("intent_name", str_join_name)
            startActivity(intent)
        }
    }
}
