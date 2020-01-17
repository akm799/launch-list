package co.uk.akm.test.launchlistcr.domain.interactor.impl

import co.uk.akm.test.launchlistcr.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistcr.domain.model.Launch
import co.uk.akm.test.launchlistcr.domain.repo.LaunchRepository

/**
 * This use case is a bit thin for our simple problem. Here we would normally combine multiple calls
 * from different repositories. In our case we just added some simple logic to list launches by type.
 * This could result in caching more data that we need, but in a more realistic case it could be useful.
 */
class ListLaunchesUseCaseImpl(private val repository: LaunchRepository) : ListLaunchesUseCase {

    override suspend fun listLaunches(type: String): List<Launch> {
        return repository.getLaunches().filter { it.type == type }
    }
}