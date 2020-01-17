package co.uk.akm.test.launchlistcr.domain.interactor

import co.uk.akm.test.launchlistcr.domain.model.Launch

interface ListLaunchesUseCase {

    suspend fun listLaunches(type: String): List<Launch>
}