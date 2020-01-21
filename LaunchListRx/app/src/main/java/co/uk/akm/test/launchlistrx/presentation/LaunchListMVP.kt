package co.uk.akm.test.launchlistrx.presentation

import androidx.annotation.StringRes
import co.uk.akm.test.launchlistrx.domain.model.Launch

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