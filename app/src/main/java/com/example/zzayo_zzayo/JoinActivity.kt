package com.example.zzayo_zzayo

// 회원가입 페이지
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class JoinActivity : AppCompatActivity() {

    // DBManager = 사용자의 이름, 아이디, 비밀번호가 존재
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 중복 아이디를 체크하기 위해 사용
    lateinit var dbManager_check: DBManager
    lateinit var sqlite_check: SQLiteDatabase

    // 회원가입 시, 사용자가 직접 입력하는 이름, 아이디, 비밀번호
    lateinit var edt_join_name: EditText
    lateinit var edt_join_id: EditText
    lateinit var edt_join_passwd: EditText

    // 회원가입 버튼
    lateinit var btn_join: Button

    var imm : InputMethodManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        edt_join_name = findViewById(R.id.edt_join_name)
        edt_join_id = findViewById(R.id.edt_join_id)
        edt_join_passwd = findViewById(R.id.edt_join_passwd)

        btn_join = findViewById(R.id.btn_join)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        dbManager = DBManager(this, "personnel", null, 1)

        //회원가입 버튼 눌렀을때 실행
        btn_join.setOnClickListener {
            var str_join_name: String = edt_join_name.text.toString()
            var str_join_id: String = edt_join_id.text.toString()
            var str_join_passwd: String = edt_join_passwd.text.toString()

            // 이름, 아이디, 비밀번호 한 곳이라도 입력하지 않은 경우
            if (str_join_name.equals("") || str_join_id.equals("") || str_join_passwd.equals("")) {
                Toast.makeText(this, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                // 새로고침
                val intent = getIntent()
                startActivity(intent)
            }
            // 이름, 아이디, 비밀번호 모두 다 입력한 경우
            else {
                //이미 존재하는 아이디인지 체크
                dbManager_check = DBManager(this, "personnel", null, 1)
                sqlite_check = dbManager_check.readableDatabase

                var cursor: Cursor
                cursor = sqlite_check.rawQuery("SELECT id FROM personnel", null)

                var id_check = 0
                while (cursor.moveToNext()) {
                    var str_id_db = cursor.getString(cursor.getColumnIndex("id")).toString()

                    // 아이디가 이미 존재하는 경우
                    if (str_join_id == str_id_db) {
                        id_check = 1
                    }
                }
                if (id_check == 1) {
                    Toast.makeText(this, "이미 존재하는 아이디입니다", Toast.LENGTH_SHORT).show()
                } else {
                    // 중복되는 아이디가 아닌 경우 DBManager에 이름, 아이디, 비밀번호를 삽입
                    sqlitedb = dbManager.writableDatabase
                    sqlitedb.execSQL("INSERT INTO personnel VALUES ('" + str_join_name + "', '" + str_join_id + "', '" + str_join_passwd + "');")
                    sqlitedb.close()

                    // 회원가입에 성공한 경우, str_join_id의 값과 함께 MainActivity로 이동
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("intent_id", str_join_id)
                    startActivity(intent)
                }
            }
        }
    }
    //이벤트 메서드 생성
    //Activity 최상위 Layout에 onClick 세팅 : 해당 레이아웃 안에서 view 클릭하면 키보드가 내려가게 함
    fun hide(v: View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }
}