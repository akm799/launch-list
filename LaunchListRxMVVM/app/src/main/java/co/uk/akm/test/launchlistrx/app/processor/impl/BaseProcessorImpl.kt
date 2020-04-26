package co.uk.akm.test.launchlistrx.app.processor.impl

import co.uk.akm.test.launchlistrx.app.processor.BaseProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CancelableViewModelObserver

abstract class BaseProcessorImpl<V, VM: BaseViewModel<*>, VMO: CancelableViewModelObserver> : BaseProcessor<V, VM> {

    protected var view: V? = null
    protected var observer: VMO? = null

    override fun attachView(view: V) {
        this.view = view
    }

    override fun cancel() {
        observer?.cancel()
    }

    override fun detachView() {
        view = null
    }

    override fun clear() {
        observer = null
    }
}