package monoxide.carbon.Helpith

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import monoxide.carbon.Helpith.API.API
import org.json.JSONArray
import org.json.JSONObject

class ListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityList = layoutInflater.inflate(R.layout.activity_list, null)

        val familiesAPI = API("families")
        val family = familiesAPI.show("5") ?: throw error("empty family!")
        val familyJSON = JSONObject(family)
        val users = familyJSON.getJSONArray("users")

        val date = intent.getStringExtra("HELPITH_DATE")
        val helpithListDataTextView: TextView = activityList.findViewById(R.id.helpith_list_date)
        helpithListDataTextView.text = date

        val listsAPI = API("lists")
        val replacedDate = date.replace('/', '-')
        val list = listsAPI.showByDate("5", replacedDate)
        println("リスト")
        println(list)

        val button: Button = activityList.findViewById(R.id.button)
        button.setOnClickListener {
            finish()
        }

        val viewGroup = activityList.findViewById<View>(R.id.tableLayout) as ViewGroup

        val tableRow = TableRow(this) as ViewGroup

        layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
        val textView = tableRow.getChildAt(0) as TextView
        textView.text = "メンバー"

        for (i in 0 until users.length()) {
            val user = users[i] as JSONObject
            val name = user.optString("name") ?: throw error("valid error name")

            layoutInflater.inflate(R.layout.helpith_list_table_row_text, tableRow)
            val nameTextView = tableRow.getChildAt(i + 1) as TextView
            nameTextView.text = name
        }

        viewGroup.addView(tableRow)

        setContentView(activityList)
    }

}