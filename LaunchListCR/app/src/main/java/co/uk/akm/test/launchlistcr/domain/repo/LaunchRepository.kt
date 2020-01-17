package co.uk.akm.test.launchlistcr.domain.repo

import co.uk.akm.test.launchlistcr.domain.model.Launch

interface LaunchRepository {

    suspend fun getLaunches(): List<Launch>
}