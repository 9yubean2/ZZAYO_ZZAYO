package com.example.zzayo_zzayo
//HomeActivity에서 두번째 버튼 눌렀을 때 실행되는 페이지
import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import java.util.*

class CreateActivity : AppCompatActivity() {

    lateinit var sqlitedb: SQLiteDatabase
    lateinit var roomDBManager : RoomDBManager

    lateinit var btn_createRoom: Button
    lateinit var edt_RoomName: EditText
    lateinit var edt_RoomPasswd: EditText
    lateinit var edt_RoomNumber: EditText
    lateinit var edt_teamNumber: EditText
    lateinit var spn_teamOrganizationMethod: Spinner
    lateinit var tv_startDate: TextView
    lateinit var tv_endDate: TextView
    lateinit var btn_startDate: Button
    lateinit var btn_endDate: Button
    lateinit var edt_RoomExplain: EditText
    lateinit var str_name: String

    lateinit var str_teamOrganizationMethod: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        spn_teamOrganizationMethod = findViewById(R.id.spn_teamOrganizationMethod)

        edt_RoomName = findViewById(R.id.edt_roomName)
        edt_RoomPasswd = findViewById(R.id.edt_roomPasswd)
        edt_RoomNumber = findViewById(R.id.edt_roomNumber)
        edt_teamNumber = findViewById(R.id.edt_teamNumber)
        edt_RoomExplain = findViewById(R.id.edt_roomExplain)

        tv_startDate = findViewById(R.id.tv_startDate)
        tv_endDate = findViewById(R.id.tv_endDate)

        btn_startDate = findViewById(R.id.btn_startDate)
        btn_endDate = findViewById(R.id.btn_endDate)
        btn_createRoom = findViewById(R.id.btn_createRoom)


        // MainActivity -> HomeActivity에서 로그인 된 이름을 전달받음
        val intent = intent
        str_name = intent.getStringExtra("intent_name").toString()

        roomDBManager = RoomDBManager(this, "room", null, 1)

        // 스피너-아답터 연결
        val items = resources.getStringArray(R.array.organizationMethod_array)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        spn_teamOrganizationMethod.adapter = myAdapter

        // 스피너 항목 선택 시 값 받아오기
        spn_teamOrganizationMethod.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    str_teamOrganizationMethod =
                        spn_teamOrganizationMethod.getItemAtPosition(position).toString()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }

        // 방 만들기 버튼 클릭 시 DB에 입력 후 RoomInfoActivity로 이동
        btn_createRoom.setOnClickListener {
            var str_userName: String = str_name
            var str_roomName: String = edt_RoomName.text.toString()
            var int_roomPasswd: Int = edt_RoomPasswd.text.toString().toInt()
            var int_roomNumber: Int = edt_RoomNumber.text.toString().toInt()
            var int_teamNumber: Int = edt_teamNumber.text.toString().toInt()
            var str_startDate: String = tv_startDate.text.toString()
            var str_endDate: String = tv_endDate.text.toString()
            var str_roomExplain: String = edt_RoomExplain.text.toString()

            sqlitedb = roomDBManager.writableDatabase
            sqlitedb.execSQL(
                "INSERT INTO room VALUES ('" + str_userName + "','"+ str_roomName + "','" + int_roomPasswd
                        + "','" + int_roomNumber + "','" + int_teamNumber + "','" + str_teamOrganizationMethod
                        + "','" + str_startDate + "','" + str_endDate + "','" + str_roomExplain + "');")
            sqlitedb.close()
            roomDBManager.close()

            val intent = Intent(this, RoomInfoActivity::class.java)
            intent.putExtra("intent_roomName", str_roomName)
            startActivity(intent)
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
                intent.putExtra("intent_name", str_name)
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
                intent.putExtra("intent_name", str_name)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}