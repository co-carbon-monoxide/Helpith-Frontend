package monoxide.carbon.Helpith

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import monoxide.carbon.Helpith.API.HelpithAPI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calenderView: CalendarView = findViewById(R.id.calendarView)
        val listener = DateChangeListener()
        calenderView.setOnDateChangeListener(listener)

        val configButton: Button = findViewById(R.id.config_button)
        configButton.setOnClickListener {
            val intent = Intent(applicationContext, ConfigActivity::class.java)
            startActivity(intent)
        }

        val evaluationButton: Button = findViewById(R.id.evaluation_button)
        evaluationButton.setOnClickListener{
            val intent = Intent(applicationContext, EvaluationActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class DateChangeListener : CalendarView.OnDateChangeListener {
        override fun onSelectedDayChange(calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
            val displayMonth = month + 1
            val intent = Intent(applicationContext, ListActivity::class.java)
            intent.putExtra("HELPITH_DATE", "$year/$displayMonth/$dayOfMonth")
            startActivity(intent)
        }
    }
}
