package co.uk.akm.test.launchlistrx.domain.model

interface LaunchDetails : Launch {

    val hasDetails: Boolean

    val details: String

    val hasSiteName: Boolean

    val siteName: String
}