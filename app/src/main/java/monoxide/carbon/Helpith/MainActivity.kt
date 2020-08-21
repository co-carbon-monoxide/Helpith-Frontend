package monoxide.carbon.Helpith

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.Button
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


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

    }

    private inner class DateChangeListener : CalendarView.OnDateChangeListener {
        override fun onSelectedDayChange(calendarView: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
            val displayMonth = month + 1
            val intent = Intent(applicationContext, ListActivity::class.java)
            intent.putExtra("HELPITH_DATE", "$year/$displayMonth/$dayOfMonth")
            startActivity(intent)
        }
    }

    inner class HitAPITask: AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            val buffer: StringBuffer


            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val stream = connection.inputStream
                reader = BufferedReader(InputStreamReader(stream))
                buffer = StringBuffer()
                var line: String?
                while (true) {
                    line = reader.readLine()
                    if (line == null) {
                        break
                    }
                    buffer.append(line)
                }

                val jsonText = buffer.toString()

                val parentJsonObj = JSONObject(jsonText)

                val parentJsonArray = parentJsonObj.getJSONArray("movies")

                val detailJsonObj = parentJsonArray.getJSONObject(0)

                val movieName: String = detailJsonObj.getString("title")

                val year: Int = detailJsonObj.getInt("year")

                return "$movieName - $year"
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            return null
        }
    }

//    override fun onPostExecute(result: String?) {
//        super.onPostExecute(result)
//        if (result == null) return
//        textView.text = result
//    }
}
