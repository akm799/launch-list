package co.uk.akm.test.launchlistrx.domain.interactor

import co.uk.akm.test.launchlistrx.domain.model.Launch
import io.reactivex.Single

interface ListLaunchesUseCase {

    fun listLaunches(type: String): Single<List<Launch>>
}