package monoxide.carbon.Helpith.API

data class HouseWorkRequest(
    val name: String,
    val time: Int,
    val list_id: Int
)

private const val rootControllerName: String = "house_works"

class HouseWorkAPI: HelpithAPI<HouseWorkRequest>(
    controllerName = rootControllerName,
    classObject = HouseWorkRequest::class.java
){}