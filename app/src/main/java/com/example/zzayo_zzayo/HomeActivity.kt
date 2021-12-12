package com.example.zzayo_zzayo

// MainActivity에서 로그인 후 넘어오는 페이지
// 버튼 3개로 구성

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    lateinit var str_id: String

    // 차례대로 팀 모아요, 팀 만들어요, 팀 들어가요
    lateinit var btn_team: Button
    lateinit var btn_create: Button
    lateinit var btn_enter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_team = findViewById(R.id.btn_team)
        btn_create = findViewById(R.id.btn_create)
        btn_enter = findViewById(R.id.btn_enter)

        // MainActivity에서 로그인 한 사용자의 아이디를 받아옴
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        // 팀 모아요 (게시판에 글을 작성해서 팀원을 모집하는 용도)
        btn_team.setOnClickListener {
            val intent = Intent(this, TeamActivity::class.java)
            intent.putExtra("intent_id", str_id)
            startActivity(intent)
        }
        // 팀 만들어요 (팀 모아요에서 팀원을 구한 후, 팀원들만이 사용할 수 있는 공간을 만드는 용도)
        btn_create.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra("intent_id", str_id)
            startActivity(intent)
        }
        // 팀 들어가요 (팀 만들어요에서 만든 공간을 비밀번호를 입력한 후 들어가는 용도)
        btn_enter.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("intent_id",str_id)
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
            // 로그아웃 옵션
            R.id.action_logout -> {
                Toast.makeText(this, "로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
            // 마이 페이지 옵션
            R.id.action_mypage -> {
                val intent = Intent(this, MyPageActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}