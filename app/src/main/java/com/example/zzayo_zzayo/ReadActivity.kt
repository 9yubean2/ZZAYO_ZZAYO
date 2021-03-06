package com.example.zzayo_zzayo

// TeamActivity에서 넘어오는 페이지
// 작성된 게시글과 댓글을 볼 수 있고, 해당 게시글에 댓글을 달 수 있음

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*

class ReadActivity : AppCompatActivity() {

    // registDBManager = 게시글의 작성자, 제목, 내용이 존재
    lateinit var registDBManager: RegistDBManager

    // commentDBManager = 댓글의 작성자, 댓글을 달려하는 게시글의 제목, 내용이 존재
    lateinit var commentDBManager: CommentDBManager

    lateinit var sqlitedb: SQLiteDatabase

    lateinit var btn_comment: Button
    lateinit var edt_comment: EditText

    lateinit var tv_title : TextView
    lateinit var tv_content : TextView
    lateinit var tv_writer : TextView

    lateinit var str_title : String
    lateinit var str_writer: String //추가
    lateinit var  str_content : String
    lateinit var  str_comment : String
    lateinit var  str_comment_writer : String
    lateinit var str_id: String

    lateinit var commentList : LinearLayout

    var imm : InputMethodManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        tv_title = findViewById(R.id.title)
        tv_content = findViewById(R.id.content)
        tv_writer = findViewById(R.id.writer)
        btn_comment = findViewById(R.id.btn_comment)
        edt_comment = findViewById(R.id.edt_comment)
        commentList = findViewById(R.id.commentList)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val face = Typeface.createFromAsset(assets, "fonta.ttf")
        val shape = GradientDrawable()
        shape.cornerRadius = 20f
        shape.setColor(Color.parseColor("#50FFAEBC"))

        //게시글 상세 보기
        val intent = intent
        //TeamActivity에서 게시글 작성자 아이디, 게시글 제목, 현재 로그인 된 사용자 아이디 받아오기
        str_writer = intent.getStringExtra("intent_writer").toString() //새로 추가
        str_title = intent.getStringExtra("intent_title").toString()
        str_id= intent.getStringExtra("intent_id").toString()

        registDBManager = RegistDBManager(this, "regist",null,1)
        sqlitedb = registDBManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT content FROM regist WHERE writer = '" + str_writer
                +"'AND title = '" + str_title + "';",null)
//        cursor = sqlitedb.rawQuery("SELECT content FROM regist WHERE writer = '" + str_id + "';",null)


        if(cursor.moveToNext()){
            str_content = cursor.getString(cursor.getColumnIndex("content")).toString()
        }
        cursor.close()
        sqlitedb.close()
        registDBManager.close()

        //제목,작성자,내용 표시
        tv_title.text = str_title
        tv_title.textSize = 30f
        tv_title.setTypeface(face)
        tv_title.setBackground(shape)

        tv_writer.text = "ID: " + str_writer //현재 로그인 아이디가 아니라 게시글 작성자 아이디
        tv_writer.textSize = 15f

        tv_content.text = str_content
        tv_content.textSize = 25f

        // commentDBManager = 댓글의 작성자, 댓글을 달려하는 게시글의 제목, 내용이 존재
        commentDBManager = CommentDBManager(this, "commentTBL", null, 1)
        sqlitedb = commentDBManager.readableDatabase

        // 댓글을 읽어오기 위한 cursor
        var cursor_comment: Cursor
        // 게시글 제목에 해당하는 댓글 가져오기
        cursor_comment = sqlitedb.rawQuery("SELECT * FROM commentTBL WHERE comment_title='"
                + str_title + "';",null)

        if(cursor_comment != null){
            var num : Int =0
            while(cursor_comment.moveToNext()){
                var str_comment_wirter = cursor_comment.getString(0).toString()
                var str_comment = cursor_comment.getString(2).toString()

                var layout_item: LinearLayout = LinearLayout(this)
                layout_item.orientation = LinearLayout.VERTICAL
                layout_item.id = num

                var tv_comment_writer : TextView = TextView(this)
                tv_comment_writer.text = str_comment_wirter
                tv_comment_writer.setTypeface(face)
                tv_comment_writer.textSize = 15f
//                tv_comment_writer.setBackgroundColor(Color.parseColor("#50FFAEBC"))
//                tv_comment_writer.setBackground(shape)

                //해당 액티비티가 자식으로 부모 액티비티에 포함된 관계에 있을 때, 의미있는 값을 반환하는 예외 처리
                if(tv_comment_writer.getParent() != null){
                    (tv_comment_writer.getParent() as ViewGroup).removeView(tv_comment_writer)
                }

                layout_item.addView(tv_comment_writer)

                var tv_comment : TextView = TextView(this)
                tv_comment.text = str_comment
                tv_comment.setTypeface(face)
                tv_comment.textSize = 20f


                var tv_null : TextView = TextView(this)
                tv_null.text="\n"
                tv_null.textSize = 10f

                //해당 액티비티가 자식으로 부모 액티비티에 포함된 관계에 있을 때, 의미있는 값을 반환하는 예외 처리
                if (tv_comment.getParent() != null) {
                    (tv_comment.getParent() as ViewGroup).removeView(tv_comment)
                }

                layout_item.addView(tv_comment)
                layout_item.addView(tv_null)
                num++
            }
        }

        cursor_comment.close()

        commentDBManager = CommentDBManager(this, "commentTBL", null, 1)

        //입력 버튼 눌렀을 때 실행
        btn_comment.setOnClickListener {

            //댓글 작성자, 댓글 다는 게시글 제목, 댓글 내용 값 변수에 저장
            var str_comment_writer: String = str_id
            var str_comment_title: String = str_title
            var str_comment: String = edt_comment.text.toString()

            sqlitedb = commentDBManager.writableDatabase

            // 댓글을 입력하지 않은 경우
            if (str_comment.equals("")) {
                Toast.makeText(this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
                val intent = getIntent()
                startActivity(intent)
            }
            // 댓글을 입력했을 경우
            else {
                //댓글 작성자, 댓글 쓰는 게시글 제목, 댓글 내용 DB에 저장
                sqlitedb.execSQL(
                    "INSERT INTO commentTBL VALUES ('" + str_comment_writer + "', '" + str_comment_title + "','"
                            + str_comment + "');"
                )
                val intent = getIntent()
                startActivity(intent)
            }
        }

        var cursor_write: Cursor
        cursor_write = sqlitedb.rawQuery("SELECT * FROM commentTBL WHERE comment_title='"
                +str_title+"';",null)

        var num : Int =0
        while(cursor_write.moveToNext()){
            var str_comment_wirter = cursor_write.getString(0).toString()
            var str_comment = cursor_write.getString(2).toString()

            var layout_item: LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id = num
            layout_item.setBackground(shape)

            var tv_comment_writer : TextView = TextView(this)
            tv_comment_writer.text = "ID: " + str_comment_wirter
            tv_comment_writer.setTypeface(face)
            tv_comment_writer.textSize = 15f
            layout_item.addView(tv_comment_writer)

            var tv_comment : TextView = TextView(this)
            tv_comment.text = str_comment
            tv_comment.setTypeface(face)
            tv_comment.textSize = 20f
            layout_item.addView(tv_comment)

            var tv_null : TextView = TextView(this)
            tv_null.text="\n"
            tv_null.textSize = 10f


            commentList.addView(layout_item)
            commentList.addView(tv_null)
            num++
        }
        cursor_write.close()
        sqlitedb.close()
        commentDBManager.close()
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
            R.id.action_logout -> {
                Toast.makeText(this, "로그아웃 성공, 로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
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