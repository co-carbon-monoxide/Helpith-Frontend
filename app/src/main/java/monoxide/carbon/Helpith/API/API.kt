package monoxide.carbon.Helpith.API

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun getCompleteUrl(url: String): String {
    val rootPath = "http://10.0.2.2:3000/api/v1/"
    return rootPath + url
}

open class HelpithAPI<T : Any?>(val controllerName: String, classObject: Class<T>) {
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val requestAdapter: JsonAdapter<T> = moshi.adapter(classObject)
    private val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

    fun index (): String? {
        val (_, _, result) = Fuel.get(getCompleteUrl(controllerName))
            .responseString()
        return when(result) {
            is Result.Failure -> {
                val ex = result.getException()
                ex.printStackTrace()
                null
            }
            is Result.Success -> {
                result.get()
            }
        }
    }

    fun show (id: Int): String? {
        val (_, _, result) = Fuel.get(getCompleteUrl("$controllerName/$id"))
            .responseString()
        println(getCompleteUrl("$controllerName/$id"))
        return when(result) {
            is Result.Failure -> {
                val ex = result.getException()
                ex.printStackTrace()
                null
            }
            is Result.Success -> {
                val res = result.get()
                println("result.get(): $res")
                result.get()
            }
        }
    }

    fun create (apiRequest: T): String? {
        val (_, _, result) = Fuel.post(getCompleteUrl(controllerName))
            .header(header)
            .body(requestAdapter.toJson(apiRequest))
            .responseString()
        return when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                ex.printStackTrace()
                null
            }
            is Result.Success -> {
                result.get()
            }
        }
    }
}