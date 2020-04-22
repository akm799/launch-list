package co.uk.akm.test.launchlistrx.app.ui.list

import androidx.annotation.StringRes
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchListStats

interface LaunchListView {

    fun displayLaunches(launches: List<Launch>, statistics: LaunchListStats)

    fun displayError(@StringRes errorResId: Int)
}