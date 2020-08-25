package monoxide.carbon.Helpith.API

import android.os.AsyncTask
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

private fun getCompleteUrl(url: String): String {
    val rootPath = "http://10.0.2.2:3000/api/v1/"
    return rootPath + url
}

class API (private val controllerName: String) {
    fun index (): String? {
        return HitAPITask().execute(getCompleteUrl(controllerName), "GET").get()
    }

    fun show (id: String): String? {
        return HitAPITask().execute(getCompleteUrl("$controllerName/$id"), "GET").get()
    }

    fun create (): String? {
        return HitAPITask().execute(getCompleteUrl(controllerName), "POST").get()
    }

    fun update (id: String): String? {
        return HitAPITask().execute(getCompleteUrl("$controllerName/$id"), "PATCH").get()
    }

    fun destroy (id: String): String? {
        return HitAPITask().execute(getCompleteUrl("$controllerName/$id"), "DELETE").get()
    }
}

class HitAPITask: AsyncTask<String, String, String>() {
    override fun doInBackground(vararg params: String?): String? {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        val buffer: StringBuffer

        try {
            val url = URL(params[0])
            val httpMethod = params[1]
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = httpMethod
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
            return jsonText
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

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result == null) return
        println(result)
    }
}