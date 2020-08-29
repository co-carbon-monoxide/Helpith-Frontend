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
import com.google.gson.JsonObject
import monoxide.carbon.Helpith.API.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class ListActivity: AppCompatActivity() {

    val handler = Handler()
    var userSize = 0
    var listId = 0

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
            startActivityForResult(intent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        val newHouseWorkName = intent?.extras?.getString("NEW_HOUSE_WORK_NAME")?: ""
        addHouseWorkRow(newHouseWorkName)
    }

    fun addHouseWorkRow (name: String) {
        val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
        val tableRow = TableRow(this) as ViewGroup
        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
        val houseWorkText = tableRow.getChildAt(0) as TextView
        houseWorkText.text = name

        for (i in 0 until userSize) {
            layoutInflater.inflate(R.layout.helpith_list_table_row_button, tableRow)
            val emptyButton = tableRow.getChildAt(i + 1) as Button
            emptyButton.text = i.toString()
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

            handler.post( Runnable () {
                val viewGroup = findViewById<View>(R.id.tableLayout) as ViewGroup
                for (i in 0 until houseWorks.length()) {
                    val houseWork = houseWorks[i] as JSONObject
                    val houseWorkName = houseWork.optString("name")
                    addHouseWorkRow(houseWorkName)
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