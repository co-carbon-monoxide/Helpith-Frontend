package monoxide.carbon.Helpith.API

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result

data class UserRequest(
    val name: String,
    val family_id: Int
)

private const val rootControllerName: String = "users"

class UserAPI: HelpithAPI<UserRequest>(
    controllerName = rootControllerName,
    classObject = UserRequest::class.java
){
    fun putName (userId: Int, name: String): String? {
        val (_, _, result) = Fuel.put(getCompleteUrl("$rootControllerName/$userId/$name"))
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