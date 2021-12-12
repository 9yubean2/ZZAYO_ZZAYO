package com.example.zzayo_zzayo

// TeamActivity에서 넘어오는 페이지
// 새로운 게시글을 작성

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class WriteActivity : AppCompatActivity() {
    // registDBManager = 게시글의 작성자, 제목, 내용이 존재
    lateinit var registDBManager: RegistDBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 사용자가 입력한 게시글의 제목, 내용
    lateinit var edt_title: EditText
    lateinit var edt_content: EditText

    // 게시글 등록 버튼
    lateinit var btn_regist: Button

    lateinit var str_id: String

    var imm : InputMethodManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        edt_title = findViewById(R.id.edt_title)
        edt_content = findViewById(R.id.edt_content)
        btn_regist = findViewById(R.id.btn_regist)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        //TeamActivity에서 현재 로그인 한 사용자 아이디를 전달받음
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        registDBManager = RegistDBManager(this, "regist", null, 1)

        //등록 버튼 눌렀을 때
        btn_regist.setOnClickListener {
            //게시글 작성자, 제목, 내용
            var str_writer: String = str_id
            var str_title: String = edt_title.text.toString()
            var str_content: String = edt_content.text.toString()

            sqlitedb = registDBManager.writableDatabase
            // 게시글 제목을 입력하지 않은 경우, 게시글 작성 불가능
            if (str_title.equals("")) {
                Toast.makeText(this, "게시글 제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                val intent = getIntent()
                startActivity(intent)
            }
            // 게시글 제목을 입력했을 경우
            else {
                sqlitedb.execSQL("INSERT INTO regist VALUES ('" + str_writer + "', '" + str_title + "', '"
                        + str_content + "')")
                sqlitedb.close()

                val intent = Intent(this, TeamActivity::class.java)
                //현재 로그인 한 사용자 아이디 (게시글 작성자 아이디)를 TeamActivity로 내보내기
                intent.putExtra("intent_id",str_id)
                startActivity(intent)
            }
        }
        registDBManager.close()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // 옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 메뉴 아이템의 id로 나누는 when문
        when(item?.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
            //로그아웃 옵션
            R.id.action_logout -> {
                Toast.makeText(this, "로그아웃 성공, 로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
            //마이페이지 옵션
            R.id.action_mypage -> {
                val intent = Intent(this, MyPageActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    //이벤트 메서드 생성
    //Activity 최상위 Layout에 onClick 세팅 : 해당 레이아웃 안에서 view 클릭하면 키보드가 내려가게 함
    fun hide(v: View){
        if(v!=null){
            imm?.hideSoftInputFromWindow(v.windowToken,0)
        }
    }
}
