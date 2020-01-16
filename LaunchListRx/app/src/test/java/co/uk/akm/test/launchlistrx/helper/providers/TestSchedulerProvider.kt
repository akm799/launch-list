package co.uk.akm.test.launchlistrx.helper.providers

import co.uk.akm.test.launchlistrx.util.providers.rx.SchedulerProvider
import io.reactivex.Scheduler

class TestSchedulerProvider(private val io: Scheduler, private val ui: Scheduler) : SchedulerProvider {

    override fun io(): Scheduler = io

    override fun ui(): Scheduler = ui
}