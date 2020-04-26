package co.uk.akm.test.launchlistrx.app.viewmodel.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class BaseViewModelObserver<T>(
    private val owner: LifecycleOwner,
    private val viewModel: BaseViewModel
) : Observer<CallResult<T>>, CancelableViewModelObserver {

    fun observe(liveData: LiveData<CallResult<T>>) {
        liveData.observe(owner, this)
    }

    override fun onChanged(t: CallResult<T>?) {
        if (t != null) {
            if (t.hasResult()) {
                onResult(t.result())
            } else {
                onError(t.error())
            }
        } else {
            onError(NullPointerException("Null call-result in callback."))
        }
    }

    abstract fun onResult(result: T)

    abstract fun onError(error: Throwable)

    override fun cancel() {
        viewModel.cancelRequestsInProgress()
    }
}