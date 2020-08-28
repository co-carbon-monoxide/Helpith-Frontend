package monoxide.carbon.Helpith.API

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result

data class ListRequest(
    val name: String,
    val date: String,
    val family_id: Int
)

private const val rootControllerName: String = "lists"

class ListAPI: HelpithAPI<ListRequest>(
    controllerName = rootControllerName,
    classObject = ListRequest::class.java
){
    fun showByDate (familyId: Int, date: String): String? {
        val (_, _, result) = Fuel.get(getCompleteUrl("$rootControllerName/$familyId/$date"))
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
}