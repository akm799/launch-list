package co.uk.akm.test.launchlistrx.app.ui.details

import androidx.annotation.StringRes
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails

interface LaunchDetailsView {

    fun displayLaunchDetails(launchDetails: LaunchDetails)

    fun displayError(@StringRes errorResId: Int)
}