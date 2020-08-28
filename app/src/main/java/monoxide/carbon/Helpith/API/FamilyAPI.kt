package monoxide.carbon.Helpith.API

data class FamilyRequest(
    val name: String
)

private const val rootControllerName: String = "families"

class FamilyAPI: HelpithAPI<FamilyRequest>(
    controllerName = rootControllerName,
    classObject = FamilyRequest::class.java
){}