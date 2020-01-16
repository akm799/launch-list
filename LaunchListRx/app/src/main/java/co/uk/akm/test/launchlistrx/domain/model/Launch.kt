package co.uk.akm.test.launchlistrx.domain.model


interface Launch {

    val flightNumber: Int

    val type: String

    val hasMissionName: Boolean

    val missionName: String

    val hasDate: Boolean

    val date: String

    val hasSuccess: Boolean

    val success: Boolean

    val hasMissionPatch: Boolean

    val missionPatch: String
}