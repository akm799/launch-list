package co.uk.akm.test.launchlistrx.data.entity.server

import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity

data class LaunchApiEntity(
    override val flight_number: Int?,
    override val mission_name: String?,
    override val launch_date_unix: Long?,
    override val launch_date_local: String?,
    val rocket: RocketEntity?,
    override val launch_success: Boolean?,
    val links: LaunchLinksEntity?
) : LaunchEntity {

    override val type: String?
        get() = rocket?.rocket_id

    override val mission_patch: String?
        get() = links?.mission_patch

    override val mission_patch_small: String?
        get() = links?.mission_patch_small
}