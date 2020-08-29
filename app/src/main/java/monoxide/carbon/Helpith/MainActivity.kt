package monoxide.carbon.Helpith

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import monoxide.carbon.Helpith.API.FamilyAPI
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    var userNames: Array<String?> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val family_id = 5

        val familyAPI = FamilyAPI()
        thread {
            val myFamily = familyAPI.show(family_id) ?: throw error("empty family!")
            val familyJson = JSONObject(myFamily)
            val usersJson = familyJson.getJSONArray("users")
            for (i in 0 until usersJson.length()) {
                val user = usersJson[i] as JSONObject
                val name = user.optString("name")
                userNames += name
            }
        }

        setCalenderViewClickEvent()
        openConfigActivityByClickEvent()
        openEvaluationActivityByClickEvent()
    }

    private fun setCalenderViewClickEvent () {
        val calenderView: CalendarView = findViewById(R.id.calendarView)
        val listener = DateChangeListener()
        calenderView.setOnDateChangeListener(listener)
    }

    private fun openConfigActivityByClickEvent () {
        val configButton: Button = findViewById(R.id.config_button)
        configButton.setOnClickListener {
            val intent = Intent(applicationContext, ConfigActivity::class.java)
            startActivity(intent)
        }
    }

    private fun openEvaluationActivityByClickEvent () {
        val evaluationButton: Button = findViewById(R.id.evaluation_button)
        evaluationButton.setOnClickListener {
            val intent = Intent(applicationContext, EvaluationActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class DateChangeListener : CalendarView.OnDateChangeListener {
        override fun onSelectedDayChange(calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
            val displayMonth = month + 1
            val intent = Intent(applicationContext, ListActivity::class.java)
            intent.putExtra("HELPITH_DATE", "$year/$displayMonth/$dayOfMonth")
            intent.putExtra("USER_NAMES", userNames)
            startActivity(intent)
        }
    }
}
