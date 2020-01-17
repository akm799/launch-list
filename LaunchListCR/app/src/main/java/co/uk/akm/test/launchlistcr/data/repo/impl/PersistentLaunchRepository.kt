package co.uk.akm.test.launchlistcr.data.repo.impl

import co.uk.akm.test.launchlistcr.data.entity.LaunchEntity
import co.uk.akm.test.launchlistcr.data.source.LaunchCache
import co.uk.akm.test.launchlistcr.data.source.LaunchDataSource
import co.uk.akm.test.launchlistcr.domain.model.Launch
import co.uk.akm.test.launchlistcr.domain.model.impl.LaunchData
import co.uk.akm.test.launchlistcr.domain.repo.LaunchRepository
import co.uk.akm.test.launchlistcr.util.date.formatInUtc

class PersistentLaunchRepository(
    private val localSource: LaunchCache,
    private val remoteSource: LaunchDataSource
) : LaunchRepository {

    override suspend fun getLaunches(): List<Launch> {
        return readOfFetchLaunches().map { it.toLaunch() }
    }

    private suspend fun readOfFetchLaunches(): List<LaunchEntity> {
        if (localSource.hasLaunches()) {
            return localSource.getLaunches()
        } else {
            return fetchValidLaunches().apply { localSource.cacheLaunches(this) }
        }
    }

    private suspend fun fetchValidLaunches(): List<LaunchEntity> {
        return remoteSource.getLaunches().filter { it.hasFlightNumberAndType() }
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
}