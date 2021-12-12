package com.example.zzayo_zzayo

// HomeActivity에서 넘어오는 페이지
// 자유게시판 (자유롭게 글을 작성하는 커뮤니티)

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TeamActivity : AppCompatActivity() {

    // registDBManager = 게시글의 작성자, 제목, 내용이 존재
    lateinit var registDBManager: RegistDBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var str_id: String

    lateinit var list_regist : LinearLayout
    lateinit var btn_write: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team)

        btn_write = findViewById(R.id.btn_write)
        list_regist = findViewById(R.id.list_regist)

        //textview 커스텀
        val face = Typeface.createFromAsset(assets, "fonta.ttf")
        val shape = GradientDrawable()
        shape.cornerRadius = 20f
        shape.setColor(Color.parseColor("#50FFAEBC"))


        // HomeActivity에서 로그인 한 사용자의 아이디를 받아옴
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        registDBManager = RegistDBManager(this,"regist",null,1)
        sqlitedb = registDBManager.readableDatabase

        // registDBManager에 있는 정보를 모두 가져옴
        var cursor: Cursor
        cursor= sqlitedb.rawQuery("SELECT * FROM regist;",null)

        if(cursor!=null){
            var num : Int =0
            while(cursor.moveToNext()){
                // registDBManager에서 읽어오고 저장
                var str_writer = cursor.getString(0).toString()
                var str_title = cursor.getString(1).toString()
                var str_content = cursor.getString(2).toString()

                var list_regist_item: LinearLayout = LinearLayout(this)
                list_regist_item.orientation = LinearLayout.VERTICAL
                list_regist_item.id = num

                var tv_title : TextView = TextView(this)
                tv_title.text = str_title
                tv_title.setTypeface(face)
                tv_title.setTextSize(30F)
                tv_title.setBackground(shape)
                tv_title.maxLines = 1
                list_regist_item.addView(tv_title)

                var tv_writer : TextView = TextView(this)
                tv_writer.text = "ID: "+str_writer
                tv_writer.setTypeface(face)
                tv_writer.setTextSize(20F)
                list_regist_item.addView(tv_writer)

                var tv_content : TextView = TextView(this)
                tv_content.text = str_content
                tv_content.setTypeface(face)
                tv_content.setTextSize(25F)
                tv_content.maxLines = 1
                list_regist_item.addView(tv_content)

                var tv_null : TextView = TextView(this)
                tv_null.text = "\n"
                tv_null.setTypeface(face)
                tv_null.setTextSize(15F)
                list_regist_item.addView(tv_null)

                list_regist.addView(list_regist_item)
                num++

                // 게시글 요소를 눌렀을때, 게시글의 상세 정보를 볼 수 있고 해당 게시글에 댓글을 쓸 수 있는 ReadActivity로 이동
                list_regist_item.setOnClickListener {
                    val intent = Intent(this,ReadActivity::class.java)
                    // 게시글의 제목과 작성자 정보 + 현재 로그인 한 사용자의 아이디를 ReadActivity에 전달
                    intent.putExtra("intent_title",str_title)
                    intent.putExtra("intent_writer",str_writer)
                    intent.putExtra("intent_id",str_id)
                    startActivity(intent)
                }
            }
            cursor.close()
            sqlitedb.close()
            registDBManager.close()
        }
        else{
            //cursor가 null일때 빈 액티비티 블러오기
            val intent = Intent(this, TeamActivity::class.java)
            startActivity(intent)
        }

        //FloatingActionButton 눌렀을 때 실행 (새로운 게시글 작성)
        btn_write.setOnClickListener {
            // 현재 로그인 한 사용자의 아이디를 WriteActivity에 전달
            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("intent_id",str_id)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //메뉴아이템의 ID로 나누는 when문
        when (item?.itemId) {
            //홈 옵션
            R.id.action_home -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
            //로그아웃 옵션
            R.id.action_logout -> {
                Toast.makeText(this, "로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
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
}