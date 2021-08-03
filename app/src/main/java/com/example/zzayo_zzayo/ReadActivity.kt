package com.example.zzayo_zzayo

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class ReadActivity : AppCompatActivity() {
    lateinit var registDBManager: RegistDBManager
    lateinit var commentDBManager: CommentDBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var btn_comment: Button
    lateinit var edt_comment: EditText


    lateinit var tvTitle : TextView
    lateinit var tvContent : TextView

    lateinit var str_title : String
    lateinit var  str_content : String
    lateinit var  str_comment : String

    lateinit var commentList : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        tvTitle = findViewById(R.id.title)
        tvContent = findViewById(R.id.content)
        btn_comment = findViewById(R.id.btn_comment)
        edt_comment = findViewById(R.id.edt_comment)
        commentList = findViewById(R.id.commentList)


        //게시글 상세 보기
        val intent = intent
        str_title = intent.getStringExtra("intent_title").toString()
        str_comment= intent.getStringExtra("intent_comment").toString()

        registDBManager = RegistDBManager(this, "registDB",null,1)
        sqlitedb = registDBManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM regist WHERE title = '" + str_title + "';",null)

        if(cursor.moveToNext()){
            str_content = cursor.getString(cursor.getColumnIndex("content")).toString()
        }

        cursor.close()
        sqlitedb.close()
        registDBManager.close()

        //제목,내용 표시
        tvTitle.text = str_title
        tvTitle.textSize = 30f
        tvTitle.setBackgroundColor(Color.LTGRAY)
        tvContent.text = str_content + "\n"

//======================================================================================================
        //댓글을 쓰고 나갔다가 들어오면 유지가 안돼있음
        //글쓴이 지금 로그인 된 사람으로 적용해야 됨
        //댓글 글쓴이도 현재 로그인 된 사람으로 적용

        commentDBManager = CommentDBManager(this, "commentTBLDB", null, 1)

        btn_comment.setOnClickListener {
            //댓글 작성해서 db에 쓰기
            sqlitedb = commentDBManager.writableDatabase

            var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM commentTBL;",null)
            //뒤로 갔다가 다시 왔을 때 commentList 부분이 유지가 안돼

            //sqlitedb.execSQL("INSERT INTO commentTBL VALUES ('" + edt_comment.text.toString() + "');")
            //intent.putExtra("intent_comment", str_comment)

            var num : Int =0

            while(cursor.moveToNext()){
                var str_comment = cursor.getString(cursor.getColumnIndex("comment")).toString()

                var layout_item: LinearLayout = LinearLayout(this)
                layout_item.orientation = LinearLayout.VERTICAL
                layout_item.id = num

                var tvComment : TextView = TextView(this)
                tvComment.text = str_comment
                tvComment.textSize = 30f
                tvComment.setBackgroundColor(Color.LTGRAY)
                layout_item.addView(tvComment)

                commentList.addView(layout_item)
                num++
            }

            cursor.close()
            sqlitedb.close()
            commentDBManager.close()

        }
    }
}