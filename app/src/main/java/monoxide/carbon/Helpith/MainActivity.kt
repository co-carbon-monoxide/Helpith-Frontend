package monoxide.carbon.Helpith

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOError
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
