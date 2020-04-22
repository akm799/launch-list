package co.uk.akm.test.launchlistrx.app.viewmodel.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {
    private var requestInProgress: Disposable? = null

    fun onRequestInitiated(request: Disposable) {
        requestInProgress = request
    }

    fun cancelRequestsInProgress() {
        requestInProgress?.dispose()
        requestInProgress = null
    }

    fun onRequestFinished() {
        requestInProgress = null
    }
}