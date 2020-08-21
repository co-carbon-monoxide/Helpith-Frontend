package monoxide.carbon.Helpith

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val date = intent.getStringExtra("HELPITH_DATE")
        val helpithListDataTextView: TextView = findViewById(R.id.helpith_list_date)
        helpithListDataTextView.text = date

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            finish()
        }
    }

}