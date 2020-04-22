package co.uk.akm.test.launchlistrx.app.processor.impl

import androidx.lifecycle.LifecycleOwner
import co.uk.akm.test.launchlistrx.app.processor.LaunchListProcessor
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.domain.model.LaunchListStats
import co.uk.akm.test.launchlistrx.domain.model.TimeInterval
import co.uk.akm.test.launchlistrx.util.date.computeTimeInterval
import co.uk.akm.test.launchlistrx.util.date.parseDate
import co.uk.akm.test.launchlistrx.util.error.DefaultErrorResolver
import co.uk.akm.test.launchlistrx.util.error.ErrorResolver
import co.uk.akm.test.launchlistrx.app.ui.list.LaunchListView
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchListViewModel
import co.uk.akm.test.launchlistrx.app.viewmodel.observers.LaunchListViewModelObserver
import co.uk.akm.test.launchlistrx.app.viewmodel.observers.impl.LaunchListViewModelObserverImpl

class LaunchListProcessorIml(
    private val errorResolver: ErrorResolver = DefaultErrorResolver()
) : BaseProcessorImpl<LaunchListView, LaunchListViewModel, LaunchListViewModelObserver>(),  LaunchListProcessor {

    override fun init(owner: LifecycleOwner, viewModel: LaunchListViewModel) {
        observer = LaunchListViewModelObserverImpl(owner, viewModel, this)
    }

    override fun listLaunches(type: String) {
        observer?.listLaunches(type)
    }

    override fun onLaunchesListed(list: List<Launch>) {
        val statistics = calculateLaunchListStats(list)
        view?.displayLaunches(list, statistics)
    }

    override fun onLaunchListError(t: Throwable) {
        view?.displayError(errorResolver.findErrorResId(t))
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