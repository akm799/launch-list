package co.uk.akm.test.launchlistrx.app.viewmodel.rxobservers

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class RxObserver<T> (
    private val parent: BaseViewModel,
    private val liveData: MutableLiveData<CallResult<T>>
): SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {
        parent.onRequestInitiated(d)
    }

    override fun onSuccess(t: T) {
        parent.onRequestFinished()
        liveData.value = CallResult(t)
    }

    override fun onError(e: Throwable) {
        parent.onRequestFinished()
        liveData.value = CallResult(e)
    }
}