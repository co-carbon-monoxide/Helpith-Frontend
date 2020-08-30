package monoxide.carbon.Helpith.API

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result

data class HouseWorkRequest(
    val name: String,
    val time: Int,
    val done: Boolean,
    val list_id: Int,
    val user_id: Int
)

private const val rootControllerName: String = "house_works"

class HouseWorkAPI: HelpithAPI<HouseWorkRequest>(
    controllerName = rootControllerName,
    classObject = HouseWorkRequest::class.java
){
    fun putDone (houseWorkId: Int): String? {
        val (_, _, result) = Fuel.put(getCompleteUrl("$rootControllerName/$houseWorkId/done"))
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