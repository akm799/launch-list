package co.uk.akm.test.launchlistrx.view.ui.list

import androidx.annotation.StringRes
import co.uk.akm.test.launchlistrx.domain.model.Launch

interface LaunchListView {

    fun displayLaunches(launches: List<Launch>)

    fun displayError(@StringRes errorResId: Int)
}