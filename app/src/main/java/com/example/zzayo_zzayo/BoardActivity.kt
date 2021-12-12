package com.example.zzayo_zzayo

// EnterActivity에서 넘어오는 페이지
// 팀원들만이 사용할 수 있는 공간

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

class BoardActivity : AppCompatActivity() {

    // boardDBManager = 공지를 등록한 사용자의 아이디, 팀 이름, 공지 내용
    lateinit var boardDBManager: BoardDBManager

    lateinit var sqlitedb: SQLiteDatabase

    lateinit var btn_comment: Button
    lateinit var edt_comment: EditText

    lateinit var str_team_title : String
    lateinit var str_board_content : String
    lateinit var str_board_writer : String
    lateinit var str_id: String

    lateinit var boardList : LinearLayout

    var imm : InputMethodManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        btn_comment = findViewById(R.id.btn_comment)
        edt_comment = findViewById(R.id.edt_comment)
        boardList = findViewById(R.id.boardList)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        val face = Typeface.createFromAsset(assets, "fonta.ttf")
        val shape = GradientDrawable()
        shape.cornerRadius = 10f
        shape.setColor(Color.parseColor("#50FFAEBC"))


        val intent = intent
        // EnterActivity에서 현재 로그인 한 사용자의 아이디, 팀 이름 받아오기
        str_id = intent.getStringExtra("intent_id").toString()
        str_team_title = intent.getStringExtra("intent_team_title").toString()

        // 공지를 등록한 사용자의 아이디, 팀 이름, 공지 내용
        boardDBManager = BoardDBManager(this, "board", null, 1)
        sqlitedb = boardDBManager.readableDatabase

        var cursor: Cursor
        //team_title에 해당는 공지 게시판 가져오기
        cursor = sqlitedb.rawQuery("SELECT * FROM board WHERE team_title='"
                + str_team_title + "';",null)
        //cursor_comment = sqlitedb.rawQuery("SELECT * FROM commentTBL;",null)

        if(cursor != null){
            var num : Int =0
            while(cursor.moveToNext()){
                var str_comment_wirter = cursor.getString(0).toString()
                var str_comment = cursor.getString(2).toString()

                var layout_item: LinearLayout = LinearLayout(this)
                layout_item.orientation = LinearLayout.VERTICAL
                layout_item.id = num
                layout_item.setBackground(shape)

                var tv_comment_writer : TextView = TextView(this)
                tv_comment_writer.text = str_comment_wirter
                tv_comment_writer.setTypeface(face)
                tv_comment_writer.textSize = 20f

                //해당 액티비티가 자식으로 부모 액티비티에 포함된 관계에 있을 때, 의미있는 값을 반환하는 예외 처리
                if(tv_comment_writer.getParent() != null){
                    (tv_comment_writer.getParent() as ViewGroup).removeView(tv_comment_writer)
                }

                layout_item.addView(tv_comment_writer)

                var tv_comment : TextView = TextView(this)
                tv_comment.text = str_comment
                tv_comment.textSize = 25f
                tv_comment_writer.setTypeface(face)

                //해당 액티비티가 자식으로 부모 액티비티에 포함된 관계에 있을 때, 의미있는 값을 반환하는 예외 처리
                if (tv_comment.getParent() != null) {
                    (tv_comment.getParent() as ViewGroup).removeView(tv_comment)
                }

                layout_item.addView(tv_comment)
                num++
            }
        }
        cursor.close()

        // 공지를 등록한 사용자의 아이디, 팀 이름, 공지 내용
        boardDBManager = BoardDBManager(this, "board", null, 1)
        //입력 버튼 눌렀을 때 실행
        btn_comment.setOnClickListener {

            // 댓글 작성자, 팀 이름, 댓글 내용 값 변수에 저장
            var str_comment_writer: String = str_id
            var str_comment_title: String = str_team_title
            var str_comment: String = edt_comment.text.toString()

            //댓글 작성해서 db에 쓰기
            sqlitedb = boardDBManager.writableDatabase

            // 댓글을 입력하지 않은 경우
            if (str_comment.equals("")) {
                Toast.makeText(this, "댓글을 입력해주세요", Toast.LENGTH_SHORT).show()
                val intent = getIntent()
                startActivity(intent)
            }
            // 댓글을 입력했을 경우
            else {
                //댓글 작성자, 댓글 달린 게시글 제목, 댓글 내용 DB에 저장
                sqlitedb.execSQL(
                    "INSERT INTO board VALUES ('" + str_comment_writer + "', '" + str_comment_title + "','"
                            + str_comment + "');"
                )

                //현재 액티비티 갱신
                val intent = getIntent()
                startActivity(intent)
            }

        }

        var cursor_write: Cursor
        cursor_write = sqlitedb.rawQuery("SELECT * FROM board WHERE team_title='"
                +str_team_title+"';",null)

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
            tv_comment_writer.textSize = 20f
            layout_item.addView(tv_comment_writer)

            var tv_comment : TextView = TextView(this)
            tv_comment.text = str_comment
            tv_comment.setTypeface(face)
            tv_comment.textSize = 25f
            layout_item.addView(tv_comment)

            var tv_null : TextView = TextView(this)
            tv_null.text="\n"
            tv_null.textSize = 10f

            boardList.addView(layout_item)
            boardList.addView(tv_null)
            num++
        }
        cursor_write.close()
        sqlitedb.close()
        boardDBManager.close()
    }

    //옵션
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //옵션 메뉴 클릭 이벤트 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //메뉴아이템의 ID로 나누는 when문
        when (item?.itemId) {
            R.id.action_home -> {
                var intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("intent_id", str_id)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                Toast.makeText(this, "로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show()
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