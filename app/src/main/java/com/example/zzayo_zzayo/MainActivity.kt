package com.example.zzayo_zzayo
//로그인 페이지
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edt_login_id: EditText
    lateinit var edt_login_passwd: EditText

    lateinit var btn_login: Button
    lateinit var btn_join: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt_login_id = findViewById(R.id.edt_login_id)
        edt_login_passwd = findViewById(R.id.edt_login_passwd)

        btn_login = findViewById(R.id.btn_login)
        btn_join = findViewById(R.id.btn_join)

        dbManager = DBManager(this, "personnel", null, 1)

        //로그인 버튼 눌렀을 때
        btn_login.setOnClickListener {
            var str_login_id: String = edt_login_id.text.toString()
            var str_login_passwd: String = edt_login_passwd.text.toString()

            sqlitedb = dbManager.readableDatabase

            var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM personnel WHERE id = '" + str_login_id + "';", null)

            while (cursor.moveToNext()) {

                var db_id: String = cursor.getString(1)
                var db_passwd: String = cursor.getString(2)

                if (str_login_id.equals(db_id)&&str_login_passwd.equals(db_passwd)) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("intent_id", str_login_id)
                    startActivity(intent)
                } else {
                    //아이디가 같고 비밀번호 틀릴때만 실행 됨
                    Toast.makeText(this, "로그인 실패, 다시 로그인 해주세요", Toast.LENGTH_SHORT).show()
                }
            }
            sqlitedb.close()
        }

        //회원가입 버튼 눌렀을 때
        btn_join.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }
}
