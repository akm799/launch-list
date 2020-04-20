package co.uk.akm.test.launchlistrx.domain.repo

import co.uk.akm.test.launchlistrx.domain.model.Launch
import io.reactivex.Single

interface LaunchRepository {

    fun getLaunches(): Single<List<Launch>>
}