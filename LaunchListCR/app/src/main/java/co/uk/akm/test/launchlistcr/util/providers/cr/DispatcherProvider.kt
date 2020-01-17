package co.uk.akm.test.launchlistcr.util.providers.cr

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}