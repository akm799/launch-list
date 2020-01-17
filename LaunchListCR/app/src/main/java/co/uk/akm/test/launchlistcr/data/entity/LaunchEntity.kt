package co.uk.akm.test.launchlistcr.data.entity


interface LaunchEntity {

    val flight_number: Int?

    val type: String?

    val mission_name: String?

    val launch_date_unix: Long?

    val launch_date_local: String?

    val launch_success: Boolean?

    val mission_patch: String?

    val mission_patch_small: String?
}