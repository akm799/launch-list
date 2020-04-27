package co.uk.akm.test.launchlistrx.app.processor.impl

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.app.processor.BaseProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.base.BaseViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CancelableViewModelObserver

abstract class BaseProcessorImpl<V, VM: BaseViewModel<*>, VMO: CancelableViewModelObserver> : BaseProcessor<V, VM> {
    private var view: V? = null
    private var observerInstance: VMO? = null

    override fun init(owner: LifecycleOwner, viewModel: VM) {
        observerInstance = observerInstance(owner, viewModel)
    }

    abstract fun observerInstance(owner: LifecycleOwner, viewModel: VM): VMO

    fun getObserver(): VMO? = observerInstance

    override fun attachView(view: V) {
        this.view = view
    }

    fun getView(): V? = view

    override fun cancel() {
        observerInstance?.cancel()
    }

    override fun detachView() {
        view = null
    }

    override fun clear() {
        observerInstance = null
    }
}