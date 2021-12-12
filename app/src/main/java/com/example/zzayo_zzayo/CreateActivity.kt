package com.example.zzayo_zzayo
// HomeActivity에서 넘어오는 페이지
// 팀 만들어요 (자유게시판에서 팀원을 구한 후, 팀을 만드는 기능)
import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import java.util.*

class CreateActivity : AppCompatActivity() {

    lateinit var sqlitedb: SQLiteDatabase
    lateinit var sqlitedb_enter: SQLiteDatabase

    // 팀에 대한 카테고리, 팀을 만든 사용자의 아이디, 팀의 이름
    // 팀 입장번호, 인원 수, 일정의 시작 날짜와 종료 날짜, 팀에 대한 설명이 존재
    lateinit var roomDBManager : RoomDBManager
    lateinit var enterDBManager: EnterDBManager

    lateinit var btn_createRoom: Button
    lateinit var edt_RoomName: EditText
    lateinit var edt_RoomPasswd: EditText
    lateinit var edt_RoomNumber: EditText
    lateinit var spn_category: Spinner
    lateinit var tv_startDate: TextView
    lateinit var tv_endDate: TextView
    lateinit var btn_startDate: Button
    lateinit var btn_endDate: Button
    lateinit var edt_RoomExplain: EditText
    lateinit var str_id: String

    lateinit var str_category: String

    var imm : InputMethodManager ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        spn_category = findViewById(R.id.spn_category)

        edt_RoomName = findViewById(R.id.edt_roomName)
        edt_RoomPasswd = findViewById(R.id.edt_roomPasswd)
        edt_RoomNumber = findViewById(R.id.edt_roomNumber)
        edt_RoomExplain = findViewById(R.id.edt_roomExplain)

        tv_startDate = findViewById(R.id.tv_startDate)
        tv_endDate = findViewById(R.id.tv_endDate)

        btn_startDate = findViewById(R.id.btn_startDate)
        btn_endDate = findViewById(R.id.btn_endDate)
        btn_createRoom = findViewById(R.id.btn_createRoom)

        //키보드 InputMethodManage 세팅
        imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?


        //HomeActivity에서 로그인 한 사용자의 아이디를 전달받음
        val intent = intent
        str_id = intent.getStringExtra("intent_id").toString()

        roomDBManager = RoomDBManager(this, "room", null, 1)

        // 스피너-아답터 연결
        val items = resources.getStringArray(R.array.category_array)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        myAdapter.setDropDownViewResource(R.layout.custom_spinner)
        spn_category.adapter = myAdapter
        spn_category

        // 스피너 항목 선택 시 값 받아오기
        spn_category.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    str_category =
                        spn_category.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        // 팀 만들기 버튼 클릭 시 DB에 입력 후 팀에 대한 정보를 확인할 수 있는 RoomInfoActivity로 이동
        btn_createRoom.setOnClickListener {
            var str_userID: String = str_id
            var str_roomName: String = edt_RoomName.text.toString()
            var str_roomPasswd: String = edt_RoomPasswd.text.toString()
            var str_roomNumber: String = edt_RoomNumber.text.toString()
            var str_startDate: String = tv_startDate.text.toString()
            var str_endDate: String = tv_endDate.text.toString()
            var str_roomExplain: String = edt_RoomExplain.text.toString()


            // 한 곳이라도 입력하지 않은 경우
            if (str_roomName.equals("") || str_roomPasswd.equals("") || str_roomNumber.equals("")
                || str_startDate.equals("") || str_endDate.equals("") || str_roomExplain.equals("")) {
                Toast.makeText(this, "모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                val intent = getIntent()
                startActivity(intent)
            }
            // 모두 다 입력했을 경우
            else {
                var int_roomPasswd: Int = str_roomPasswd.toInt()
                var int_roomNumber: Int = str_roomNumber.toInt()

                sqlitedb = roomDBManager.writableDatabase
                sqlitedb.execSQL(
                    "INSERT INTO room VALUES ('" + str_category + "','" + str_userID + "','"
                            + str_roomName + "','" + int_roomPasswd + "','" + int_roomNumber + "','"
                            + str_startDate + "','" + str_endDate + "','" + str_roomExplain + "');")
                sqlitedb.close()
                roomDBManager.close()

                enterDBManager = EnterDBManager(this, "enter", null, 1)
                sqlitedb_enter = enterDBManager.writableDatabase
                sqlitedb_enter.execSQL("INSERT INTO enter VALUES ('" + str_id + "','" + str_roomName
                        + "','" + int_roomPasswd + "','" + str_roomExplain +"');")

                sqlitedb_enter.close()
                enterDBManager.close()

                //RoomInfoActivity로 로그인 된 이름과 방 이름을 전달함
                val intent = Intent(this, RoomInfoActivity::class.java)
                intent.putExtra("intent_roomName", str_roomName)
                intent.putExtra("intent_id",str_id)
                startActivity(intent)
            }
        }

        // startDate버튼 클릭 시 시작일 선택
        btn_startDate.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)
            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    tv_startDate.setText("${year}년  ${month + 1}월  ${dayOfMonth}일")
                }
            }, year, month, date)
            dlg.show()
        }

        // endDate버튼 클릭 시 종료일 선택
        btn_endDate.setOnClickListener {
            val today = GregorianCalendar()
            val year: Int = today.get(Calendar.YEAR)
            val month: Int = today.get(Calendar.MONTH)
            val date: Int = today.get(Calendar.DATE)
            val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    tv_endDate.setText("${year}년  ${month + 1}월  ${dayOfMonth}일")
                }
            }, year, month, date)
            dlg.show()
        }

    }

    // 옵션 메뉴를 액티비티에 표시
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