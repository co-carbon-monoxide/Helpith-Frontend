package monoxide.carbon.Helpith

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import monoxide.carbon.Helpith.API.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class ListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityList = layoutInflater.inflate(R.layout.activity_list, null)

        val date = intent.getStringExtra("HELPITH_DATE")
        val helpithListDataTextView: TextView = activityList.findViewById(R.id.helpith_list_date)
        helpithListDataTextView.text = date

        val button: Button = activityList.findViewById(R.id.button)
        button.setOnClickListener {
            finish()
        }

        val viewGroup = activityList.findViewById<View>(R.id.tableLayout) as ViewGroup
        val tableRow = TableRow(this) as ViewGroup
        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
        val textView = tableRow.getChildAt(0) as TextView
        textView.text = "メンバー"

        val handler = Handler()

        setContentView(activityList)

        thread {
            val familyAPI = FamilyAPI()
            val myFamily = familyAPI.show(5) ?: throw error("empty family!")
            val familyJson = JSONObject(myFamily)
            val users = familyJson.getJSONArray("users")

            val listAPI = ListAPI()
            val replacedDate = date.replace('/', '-')
            val list = listAPI.showByDate(5, replacedDate)

            for (i in 0 until users.length()) {
                val user = users[i] as JSONObject
                val name = user.optString("name") ?: throw error("valid error name")

                handler.post( Runnable() {
                    layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
                    val nameTextView = tableRow.getChildAt(i + 1) as TextView
                    nameTextView.text = name
                })
            }
        }

        viewGroup.addView(tableRow)



//        val houseWorkAPI = API("house_works")
//        val houseWorkEditText: EditText = activityList.findViewById(R.id.house_work_edit_text)
//        val addHouseWorkButton: Button = activityList.findViewById(R.id.add_house_work_button)
//        addHouseWorkButton.setOnClickListener {
//            val text = houseWorkEditText.text
//            if (!TextUtils.isEmpty(text)) {
//                println("記入されています: $text")
//                houseWorkAPI.create(
//                    """{"name": "testHouseWork","time": "20","list_id": "1"}"""
//                )
//                houseWorkEditText.text.clear()
//            }
//        }
    }

}