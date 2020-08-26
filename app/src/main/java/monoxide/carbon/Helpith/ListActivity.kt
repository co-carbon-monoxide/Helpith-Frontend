package monoxide.carbon.Helpith

import android.os.Bundle
import android.text.Layout
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

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

        // val tableRowHeader: TableRow = activityList.findViewById(R.id.helpith_list_table_row_header)
        // val helpithListTable = layoutInflater.inflate(R.layout.helpith_list_table_row_text, null)

        val tableLayout: TableLayout = activityList.findViewById(R.id.tableLayout)
        val tableRowHeaderParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        val tableRowHeader = TableRow(this).also {
            val textView = TextView(this)
            val textViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            textViewParams.weight = 1f
            textView.text = "メンバー"
            it.addView(textView, textViewParams)
        }

        tableLayout.addView(tableRowHeader, tableRowHeaderParams)

        val tv = TextView(this)
        tv.text = "ttttt"
        activityList.findViewById<ConstraintLayout>(R.id.helpith_list_constraint_layout).addView(tv)

        setContentView(activityList)
    }

}