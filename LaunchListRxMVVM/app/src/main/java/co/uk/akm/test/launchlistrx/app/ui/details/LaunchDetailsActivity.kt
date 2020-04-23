package co.uk.akm.test.launchlistrx.app.ui.details

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.app.processor.LaunchDetailsProcessor
import co.uk.akm.test.launchlistrx.app.viewmodel.LaunchDetailsViewModel
import co.uk.akm.test.launchlistrx.domain.model.LaunchDetails
import co.uk.akm.test.launchlistrx.util.date.reformatAsDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_launch_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchDetailsActivity : AppCompatActivity(), LaunchDetailsView {
    companion object {
        private const val FLIGHT_NUMBER_KEY = "LaunchDetailsActivity.Flight.Number"

        fun start(parent: Activity, flightNumber: Int) {
            val intent = Intent(parent, LaunchDetailsActivity::class.java).apply {
                putExtra(FLIGHT_NUMBER_KEY, flightNumber)
            }

            parent.startActivity(intent)
        }
    }

    private val viewModel: LaunchDetailsViewModel by viewModel()
    private val processor: LaunchDetailsProcessor by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_details)

        processor.init(this, viewModel)
    }

    override fun onResume() {
        super.onResume()

        processor.attachView(this)
        processor.getLaunchDetails(getFlightNumber())
    }

    private fun getFlightNumber(): Int {
        return intent.getIntExtra(FLIGHT_NUMBER_KEY, 0)
    }

    override fun onPause() {
        try {
            processor.also {
                it.cancel()
                it.detachView()
            }
        } finally {
            super.onPause()
        }
    }

    override fun displayLaunchDetails(launchDetails: LaunchDetails) {
        launchDetailsTitle.text = launchDetails.missionName

        Glide.with(this)
            .load(launchDetails.missionPatch)
            .error(R.drawable.ic_launcher_foreground)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(launcDetailsMissionPatch)

        if (launchDetails.hasDate) {
            launchDetailsDate.text = resources.getString(R.string.launch_date, reformatAsDate(launchDetails.date))
        } else {
            launchDetailsDate.setText(R.string.unknown_launch_date)
        }

        launchDetailsSite.text = if (launchDetails.hasSiteName) launchDetails.siteName else "Unknown"

        if (launchDetails.hasSuccess) {
            launchDetailsOutcome.text = if (launchDetails.success) "Success" else "Failure"
        } else {
            launchDetailsOutcome.text = "Unknown"
        }

        if (launchDetails.hasDetails) {
            launchDetailsSummary.text = launchDetails.details
        }
    }

    override fun displayError(errorResId: Int) {
        launchDetailsTitle.setText(errorResId)
    }

    override fun onDestroy() {
        try {
            processor.clear()
        } finally {
            super.onDestroy()
        }
    }
}
