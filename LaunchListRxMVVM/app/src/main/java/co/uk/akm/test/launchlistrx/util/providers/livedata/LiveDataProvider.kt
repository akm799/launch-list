package co.uk.akm.test.launchlistrx.util.providers.livedata

import androidx.lifecycle.MutableLiveData
import co.uk.akm.test.launchlistrx.app.viewmodel.base.CallResult

interface LiveDataProvider {
    fun <T> liveDataInstance(): MutableLiveData<CallResult<T>>
}