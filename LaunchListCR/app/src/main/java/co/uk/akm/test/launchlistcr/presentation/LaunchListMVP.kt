package co.uk.akm.test.launchlistcr.presentation

import androidx.annotation.StringRes
import co.uk.akm.test.launchlistcr.domain.model.Launch

interface LaunchListMVP {

    interface View {

        fun displayLaunches(launches: List<Launch>)

        fun displayError(@StringRes errorResId: Int)
    }

    interface Presenter {

        fun attachView(view: View)

        fun listLaunches(type: String)

        fun cancelRequestsInProgress()

        fun detachView()
    }
}