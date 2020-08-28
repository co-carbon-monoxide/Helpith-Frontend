package monoxide.carbon.Helpith.API

data class HouseWorkRequest(
    val name: String
)

private const val rootControllerName: String = "house_works"

class HouseWorkAPI: HelpithAPI<HouseWorkRequest>(
    controllerName = rootControllerName,
    classObject = HouseWorkRequest::class.java
){}