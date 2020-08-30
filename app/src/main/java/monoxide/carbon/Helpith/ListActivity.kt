package monoxide.carbon.Helpith

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import monoxide.carbon.Helpith.API.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import kotlin.concurrent.thread

class ListActivity: AppCompatActivity() {

    val handler = Handler()
    var userSize = 0
    var listId = 0
    var usersJson = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        *   やらなきゃいけないこと
        *　　・日付からその日のリストを取得
        *   ・ボタンが押されたら家事を追加
        * 　 ・マス目のボタンが押されたら家事タスクの done/not done
         */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Helpith リスト"

        // 日付をセット
        val date = intent.getStringExtra("HELPITH_DATE")
        val helpithListDataTextView: TextView = findViewById(R.id.helpith_list_date)
        helpithListDataTextView.text = date
        // end

        val usersJsonString = intent.getStringExtra("USERS_JSON")
        usersJson = JSONArray(usersJsonString)
        println(usersJsonString)

        // 家族をセット
        val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
        val tableRow = TableRow(this) as ViewGroup
        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
        val textView = tableRow.getChildAt(0) as TextView
        textView.text = "メンバー"

        val userNames = intent.getStringArrayExtra("USER_NAMES") ?: throw error("Empty USER_NAMES")
        userSize = userNames.size
        for (i in 0 until userSize) {
            layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
            val nameTextView = tableRow.getChildAt(i + 1) as TextView
            nameTextView.text = userNames[i]
        }
        viewGroup.addView(tableRow)
        // end

        if (date != null) {
            displayHelpithList(date)
        }

        val openHouseWorkButton: Button = findViewById(R.id.open_house_work_button)
        openHouseWorkButton.setOnClickListener {
            val intent = Intent(applicationContext, NewHouseWorkActivity::class.java)
            intent.putExtra("LISTID", listId)
            intent.putExtra("USER_NAME", userNames)
            intent.putExtra("USERS_JSON", usersJsonString)
            startActivityForResult(intent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            val newHouseWorkName = intent?.extras?.getString("NEW_HOUSE_WORK_NAME")?: ""
            val newHouseWorkRes = intent?.extras?.getString("NEW_HOUSE_WORK_RES")?: ""
            val newHouseWorkResJson = JSONObject(newHouseWorkRes)
            addHouseWorkRow(newHouseWorkResJson)
        }
    }

    fun addHouseWorkRow (houseWork: JSONObject) {
        val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
        val tableRow = TableRow(this) as ViewGroup
        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
        val houseWorkText = tableRow.getChildAt(0) as TextView
        val name = houseWork.optString("name")
        val houseWorkId = houseWork.optInt("id")
        val userIdByHouseWork = houseWork.optInt("user_id")
        val houseWorkDone = houseWork.optBoolean("done")
        houseWorkText.text = name

        println(houseWork)

        for (i in 0 until userSize) {
            val user = usersJson[i] as JSONObject
            val user_id = user.optInt("id")

            println("check")
            println(user)

            if (user_id == userIdByHouseWork) {
                if (!houseWorkDone) {
                    layoutInflater.inflate(R.layout.helpith_list_table_row_button, tableRow)
                    val checkButton = tableRow.getChildAt(i + 1) as Button
                    checkButton.text = "未達成"
                    checkButton.setOnClickListener {
                        val houseWorkAPI = HouseWorkAPI()
                        thread {
                            houseWorkAPI.putDone(houseWorkId)
                        }
                        checkButton.isClickable = false
                        checkButton.text = "完了！"
                        Toast.makeText(applicationContext, "「$name」が終わりました！お疲れ様！", Toast.LENGTH_LONG).show()
                    }
                } else {
                    layoutInflater.inflate(R.layout.helpith_list_table_row_button, tableRow)
                    val doneButton = tableRow.getChildAt(i + 1) as Button
                    doneButton.text = "完了！"
                    doneButton.isClickable = false
                }
            } else {
                layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
                val dummyText = tableRow.getChildAt(i + 1) as TextView
                dummyText.text = ""
            }
        }
        viewGroup.addView(tableRow)
    }

    fun displayHelpithList (date: String) {
        thread {
            val listAPI = ListAPI()
            val replacedDate = date.replace('/', '-')
            val todayList = listAPI.showByDate(5, replacedDate)
            val listJson = JSONObject(todayList)
            val houseWorks = listJson.getJSONArray("house_works")
            listId = listJson.optString("id").toInt()

            println(listJson)

            handler.post( Runnable () {
                val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
                for (i in 0 until houseWorks.length()) {
                    val houseWork = houseWorks[i] as JSONObject
                    addHouseWorkRow(houseWork)
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}