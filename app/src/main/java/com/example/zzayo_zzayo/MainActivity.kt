package com.example.zzayo_zzayo
//로그인 페이지
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

class MainActivity : AppCompatActivity() {

    // DBManager = 사용자의 이름, 아이디, 비밀번호가 존재
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    //로그인 시, 사용자가 직접 입력하는 아이디와 비밀번호
    lateinit var edt_login_id: EditText
    lateinit var edt_login_passwd: EditText

    //로그인버튼
    lateinit var btn_login: Button

    //회원가입 버튼
    lateinit var btn_join: Button

    var imm : InputMethodManager?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt_login_id = findViewById(R.id.edt_login_id)
        edt_login_passwd = findViewById(R.id.edt_login_passwd)

        btn_login = findViewById(R.id.btn_login)
        btn_join = findViewById(R.id.btn_join)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        dbManager = DBManager(this, "personnel", null, 1)

        //로그인 버튼 눌렀을 때
        btn_login.setOnClickListener {
            var str_login_id: String = edt_login_id.text.toString()
            var str_login_passwd: String = edt_login_passwd.text.toString()

            sqlitedb = dbManager.readableDatabase

            //사용자가 로그인 화면에서 입력한 아이디인 str_login_id 값을 통해 DBManager에서 해당되는 비밀번호를 가져옴
            var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM personnel WHERE id = '" + str_login_id + "';", null)

            while (cursor.moveToNext()) {

                var db_id: String = cursor.getString(1)
                var db_passwd: String = cursor.getString(2)

                // 아이디와 비밀번호 한 곳이라도 입력하지 않은 경우
                if (str_login_id.equals("") || str_login_passwd.equals("")) {
                    Toast.makeText(this, "로그인 실패, 다시 로그인 해주세요", Toast.LENGTH_SHORT).show()
                    //새로고침
                    val intent = getIntent()
                    startActivity(intent)
                }
                // 아이디와 비밀번호 모두 다 입력했을 경우
                else {
                    // 사용자가 입력한 아이디, 비밀번호와 DBManager에 있는 아이디, 비밀번호가 서로 일치하는지 비교
                    if (str_login_id.equals(db_id)) {
                        if (str_login_passwd.equals(db_passwd)) {
                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                            // 로그인에 성공한 경우, str_login_id의 값과 함께 HomeActivity로 이동
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.putExtra("intent_id", str_login_id)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, "로그인 실패, 다시 로그인 해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        Toast.makeText(this, "로그인 실패, 다시 로그인 해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // 입력한 아이디가 DBManager에 존재하지 않는 경우
            if (cursor.count <= 0) {
                Toast.makeText(this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()
            }

            sqlitedb.close()
        }

        //회원가입 버튼 눌렀을 때, JoinActivity로 이동해 회원가입을 진행
        btn_join.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
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
