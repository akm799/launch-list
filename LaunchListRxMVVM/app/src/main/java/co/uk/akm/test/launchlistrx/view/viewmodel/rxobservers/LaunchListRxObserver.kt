package co.uk.akm.test.launchlistrx.view.viewmodel.rxobservers

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.view.viewmodel.base.CallResult
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

class LaunchListRxObserver (
    private val parent: LaunchViewModel,
    private val liveData: MutableLiveData<CallResult<List<Launch>>>
): SingleObserver<List<Launch>> {

    override fun onSubscribe(d: Disposable) {
        parent.onRequestInitiated(d)
    }

    override fun onSuccess(t: List<Launch>) {
        parent.onRequestFinished()
        liveData.value =
            CallResult(t)
    }

    override fun onError(e: Throwable) {
        parent.onRequestFinished()
        liveData.value =
            CallResult(e)
    }
}