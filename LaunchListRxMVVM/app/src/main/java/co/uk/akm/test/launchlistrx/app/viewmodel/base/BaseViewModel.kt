package co.uk.akm.test.launchlistrx.app.viewmodel.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.uk.akm.test.launchlistrx.util.providers.livedata.LiveDataProvider
import io.reactivex.disposables.Disposable

open class BaseViewModel<T>(liveDataProvider: LiveDataProvider) : ViewModel() {
    protected val liveData: MutableLiveData<CallResult<T>> = liveDataProvider.liveDataInstance()

    private var requestInProgress: Disposable? = null

    fun onRequestInitiated(request: Disposable) {
        requestInProgress = request
    }

    /**
     * Meant to be invoked if the user explicitly wishes to cancel an ongoing request.
     */
    fun cancelRequestsInProgress() {
        requestInProgress?.dispose()
        requestInProgress = null
    }

    fun onRequestFinished(result: CallResult<T>) {
        requestInProgress = null
        liveData.value = result
    }
}