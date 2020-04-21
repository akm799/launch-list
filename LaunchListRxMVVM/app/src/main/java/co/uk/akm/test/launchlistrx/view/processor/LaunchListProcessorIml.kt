package co.uk.akm.test.launchlistrx.view.processor

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchListStats
import co.uk.akm.test.launchlistrx.domain.model.TimeInterval
import co.uk.akm.test.launchlistrx.util.date.computeTimeInterval
import co.uk.akm.test.launchlistrx.util.date.parseDate
import co.uk.akm.test.launchlistrx.util.error.DefaultErrorResolver
import co.uk.akm.test.launchlistrx.util.error.ErrorResolver
import co.uk.akm.test.launchlistrx.view.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.view.viewmodel.LaunchViewModel
import co.uk.akm.test.launchlistrx.view.viewmodel.observers.LaunchListViewModelObserver
import co.uk.akm.test.launchlistrx.view.viewmodel.observers.LaunchListViewModelObserverImpl

class LaunchListProcessorIml(
    private val errorResolver: ErrorResolver = DefaultErrorResolver()
) : LaunchListProcessor {

    private var view: LaunchListView? = null
    private lateinit var observer: LaunchListViewModelObserver

    override fun init(owner: LifecycleOwner, viewModel: LaunchViewModel) {
        observer = LaunchListViewModelObserverImpl(owner, viewModel, this)
    }

    override fun attachView(view: LaunchListView) {
        this.view = view
    }

    override fun listLaunches(type: String) {
        observer.listLaunches(type)
    }

    override fun onLaunchesListed(list: List<Launch>) {
        val statistics = calculateLaunchListStats(list)
        view?.displayLaunches(list, statistics)
    }

    override fun onLaunchListError(t: Throwable) {
        view?.displayError(errorResolver.findErrorResId(t))
    }

    override fun cancel() {
        observer.cancel()
    }

    override fun detachView() {
        view = null
    }

    // This method simulates some business logic.
    private fun calculateLaunchListStats(list: List<Launch>): LaunchListStats {
        val successPercentage = findSuccessPercentage(list)
        val meanTimeBetweenLaunches = findMeanTimeBetweenLaunches(list)

        return LaunchListStats(successPercentage, meanTimeBetweenLaunches)
    }

    private fun findSuccessPercentage(list: List<Launch>): Int {
        val ofKnownOutcome = list.filter { it.hasSuccess }
        val rate = ofKnownOutcome.count { it.success }/ofKnownOutcome.size.toFloat()

        return Math.round(100*rate)
    }

    private fun findMeanTimeBetweenLaunches(list: List<Launch>): TimeInterval {
        val launchTimes = list.filter { it.hasDate }.map { parseDate(it.date).time }

        var totalMillis = 0L
        for (i in 1 until launchTimes.size) {
            totalMillis += Math.abs(launchTimes[i] - launchTimes[i - 1])
        }

        return computeTimeInterval(totalMillis/launchTimes.size)
    }
}