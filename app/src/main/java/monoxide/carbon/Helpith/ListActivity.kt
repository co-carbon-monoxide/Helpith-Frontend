package monoxide.carbon.Helpith

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.JsonObject
import monoxide.carbon.Helpith.API.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class ListActivity: AppCompatActivity() {

    val handler = Handler()
    var userSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        /*
        *   やらなきゃいけないこと
        *　　・日付からその日のリストを取得
        *   ・ボタンが押されたら家事を追加
        * 　 ・マス目のボタンが押されたら家事タスクの done/not done
         */
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // 日付をセット
        val date = intent.getStringExtra("HELPITH_DATE")
        val helpithListDataTextView: TextView = findViewById(R.id.helpith_list_date)
        helpithListDataTextView.text = date
        // end

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

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            finish()
        }

        if (date != null) {
            displayHelpithList(date)
        }
    }

    fun displayHelpithList (date: String) {
        thread {
            val listAPI = ListAPI()
            val replacedDate = date.replace('/', '-')
            val todayList = listAPI.showByDate(5, replacedDate)
            val listJson = JSONObject(todayList)
            println(listJson)
            val houseWorks = listJson.getJSONArray("house_works")
            val listId = listJson.optString("id").toInt()
            setAddHouseWork(listId)

            handler.post( Runnable () {
                val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
                for (i in 0 until houseWorks.length()) {
                    val houseWork = houseWorks[i] as JSONObject
                    val houseWorkName = houseWork.optString("name")
                    val tableRow = TableRow(this) as ViewGroup
                    layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
                    val textView = tableRow.getChildAt(0) as TextView
                    textView.text = houseWorkName

                    for (j in 0 until userSize) {
                        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
                        val emptyTextView = tableRow.getChildAt(j + 1) as TextView
                        emptyTextView.text = ""
                    }
                    viewGroup.addView(tableRow)
                }
            })
        }
    }

    fun setAddHouseWork (listId: Int) {
        val houseWorkAPI = HouseWorkAPI()
        val houseWorkEditText: EditText = findViewById(R.id.house_work_edit_text)
        val addHouseWorkButton: Button = findViewById(R.id.add_house_work_button)
        val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
        handler.post( Runnable () {
            addHouseWorkButton.setOnClickListener {
                val text = houseWorkEditText.text
                val houseWorkRequest = HouseWorkRequest(
                    name = text.toString(),
                    time = 20,
                    list_id = listId
                )
                if (!TextUtils.isEmpty(text)) {
                    thread {
                        houseWorkAPI.create(houseWorkRequest)
                    }

                    val tableRow = TableRow(this) as ViewGroup
                    layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
                    val textView = tableRow.getChildAt(0) as TextView
                    textView.text = text.toString()

                    println("-------------------------------- $userSize")
                    for (i in 0 until userSize) {
                        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
                        val emptyTextView = tableRow.getChildAt(i + 1) as TextView
                        emptyTextView.text = ""
                    }
                    viewGroup.addView(tableRow)

                    houseWorkEditText.text.clear()
                }
            }
        })
    }
}