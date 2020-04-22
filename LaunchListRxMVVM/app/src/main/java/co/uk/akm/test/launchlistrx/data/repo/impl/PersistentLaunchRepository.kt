package co.uk.akm.test.launchlistrx.data.repo.impl

import co.uk.akm.test.launchlistrx.data.entity.LaunchDetailsEntity
import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity
import co.uk.akm.test.launchlistrx.data.source.LaunchCache
import co.uk.akm.test.launchlistrx.data.source.LaunchDataSource
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails
import co.uk.akm.test.launchlistrx.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistrx.domain.model.impl.LaunchDetailsData
import co.uk.akm.test.launchlistrx.domain.repo.LaunchRepository
import co.uk.akm.test.launchlistrx.util.date.formatInUtc
import co.uk.akm.test.launchlistrx.util.rx.readOrFetchEntity
import co.uk.akm.test.launchlistrx.util.rx.readOrTryToFetchEntity
import io.reactivex.Single

class PersistentLaunchRepository(
    private val localSource: LaunchCache,
    private val remoteSource: LaunchDataSource
) : LaunchRepository {

    override fun getLaunches(): Single<List<Launch>> {
        return readOfFetchLaunches().map { entities -> entities.map { it.toLaunch() } }
    }

    private fun readOfFetchLaunches(): Single<List<LaunchEntity>> {
        val hasLaunches = { localSource.hasLaunches() }
        val readLaunches = { localSource.getLaunches() }
        val fetchLaunches = { fetchValidLaunches() }
        val cacheLaunches = { launches: List<LaunchEntity> ->  localSource.cacheLaunches(launches) }

        return readOrFetchEntity(hasLaunches, readLaunches, fetchLaunches, cacheLaunches)
    }

    private fun fetchValidLaunches(): Single<List<LaunchEntity>> {
        return remoteSource.getLaunches().map { entities -> entities.filter { it.hasFlightNumberAndType() } }
    }

    private fun LaunchEntity.hasFlightNumberAndType(): Boolean {
        return (flight_number != null && type != null)
    }

    private fun LaunchEntity.toLaunch(): Launch {
        val flightNumber = flight_number ?: throw IllegalArgumentException("Property 'flight_number' cannot be null.")
        val launchType = type ?: throw IllegalArgumentException("Property 'type' cannot be null.")
        val launchDate = launch_date_local ?: launch_date_unix?.let { formatInUtc(it) } // Better the UTC launch date returned than nothing at all.
        val missionPatch = mission_patch_small ?: mission_patch // Prefer the small mission patch to save space.

        return LaunchData(flightNumber, launchType, mission_name, launchDate, launch_success, missionPatch)
    }

    override fun getLaunchDetails(flightNumber: Int): Single<LaunchDetails?> {
        return readOfFetchLaunchDetails(flightNumber).map { it.toLaunchDetails() }
    }

    private fun readOfFetchLaunchDetails(flightNumber: Int): Single<LaunchDetailsEntity?> {
        val hasLaunchDetails = { localSource.hasLaunchDetails(flightNumber) }
        val readLaunchDetails = { localSource.getLaunchDetails(flightNumber) }
        val fetchLaunchDetails = { fetchValidLaunchDetails(flightNumber) }
        val cacheLaunchDetails = { launchDetails: LaunchDetailsEntity ->  localSource.cacheLaunchDetails(launchDetails) }

        return readOrTryToFetchEntity(hasLaunchDetails, readLaunchDetails, fetchLaunchDetails, cacheLaunchDetails)
    }

    private fun fetchValidLaunchDetails(flightNumber: Int): Single<LaunchDetailsEntity?> {
        return remoteSource.getLaunchDetails(flightNumber).map { if (it.hasFlightNumberAndType()) it else null  }
    }

    private fun LaunchDetailsEntity.hasFlightNumberAndType(): Boolean {
        return (flight_number != null && type != null)
    }

    private fun LaunchDetailsEntity.toLaunchDetails(): LaunchDetails {
        val flightNumber = flight_number ?: throw IllegalArgumentException("Property 'flight_number' cannot be null.")
        val launchType = type ?: throw IllegalArgumentException("Property 'type' cannot be null.")
        val launchDate = launch_date_local ?: launch_date_unix?.let { formatInUtc(it) } // Better the UTC launch date returned than nothing at all.
        val missionPatch = mission_patch_small ?: mission_patch // Prefer the small mission patch to save space.

        return LaunchDetailsData(flightNumber, launchType, mission_name, launchDate, launch_success, missionPatch, details, site_name_long)
    }
}