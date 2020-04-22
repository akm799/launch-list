package co.uk.akm.test.launchlistrx.domain.model.impl

import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails
import java.util.*

class LaunchDetailsData(
    override val flightNumber: Int,
    override val type: String,
    private val missionNameData: String?,
    private val dateData: String?,
    private val successData: Boolean?,
    private val missionPatchData: String?,
    private val detailsData: String?,
    private val siteNameData: String?
) : LaunchDetails {

    override val hasMissionName: Boolean = (missionNameData != null)

    override val missionName: String
        get() = missionNameData ?: error("MissionName")

    override val hasDate: Boolean = (dateData != null)

    override val date: String
        get() = dateData ?: error("Date")

    override val hasSuccess: Boolean = (successData != null)

    override val success: Boolean
        get() = successData ?: error("Success")

    override val hasMissionPatch: Boolean = (missionPatchData != null)

    override val missionPatch: String
        get() = missionPatchData ?: error("MissionPatch")

    override val hasDetails: Boolean
        get() = (detailsData != null)

    override val details: String
        get() = detailsData ?: error("Details")

    override val hasSiteName: Boolean
        get() = (siteNameData != null)

    override val siteName: String
        get() = siteNameData ?: error("SiteName")

    private fun error(fieldName: String): Nothing {
        throw IllegalStateException("Please ensure that 'has$fieldName' is true before accessing the ${fieldName.toLowerCase(Locale.UK)} property.")
    }
}