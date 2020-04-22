package co.uk.akm.test.launchlistrx.domain.interactor.impl

import co.uk.akm.test.launchlistrx.domain.interactor.ListLaunchesUseCase
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails
import co.uk.akm.test.launchlistrx.domain.repo.LaunchRepository
import io.reactivex.Single

/**
 * This use case is a bit thin for our simple problem. Here we would normally combine multiple calls
 * from different repositories. In our case we just added some simple logic to list launches by type.
 * This could result in caching more data that we need, but in a more realistic case it could be useful.
 */
class ListLaunchesUseCaseImpl(private val repository: LaunchRepository) : ListLaunchesUseCase {

    override fun listLaunches(type: String): Single<List<Launch>> {
        return repository.getLaunches().map { launches ->
            launches.filter { it.type == type }
        }
    }

    override fun getLaunchDetails(flightNumber: Int): Single<LaunchDetails> = repository.getLaunchDetails(flightNumber)
}