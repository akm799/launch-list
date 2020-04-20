package co.uk.akm.test.launchlistrx.helper.matchers.custom

import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.entity.server.LaunchApiEntity


class LaunchDbEntityListMatcher(expectedFlightNumbers: List<Int>) : AbstractLaunchListMatcher<LaunchDbEntity>(expectedFlightNumbers) {
    override fun extractFlightNumber(item: LaunchDbEntity): Int? = item.flight_number
}

class LaunchApiEntityListMatcher(expectedFlightNumbers: List<Int>) : AbstractLaunchListMatcher<LaunchApiEntity>(expectedFlightNumbers) {
    override fun extractFlightNumber(item: LaunchApiEntity): Int? = item.flight_number
}
