package co.uk.akm.test.launchlistrx.presentation.ui.list

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.domain.model.Launch
import co.uk.akm.test.launchlistrx.util.date.reformatAsDate
import com.bumptech.glide.Glide

class LaunchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindLaunch(launch: Launch) {
        val resources = itemView.resources
        bindMissionPatch(launch)
        bindNameAndDate(resources, launch)
        bindOutcome(launch)
    }

    private fun bindMissionPatch(launch: Launch) {
        if (launch.hasMissionPatch) {
            setMissionPatch(launch)
        } else {
            itemView.findViewById<ImageView>(R.id.missionPatch).setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    private fun setMissionPatch(launch: Launch) {
        Glide.with(itemView)
            .load(launch.missionPatch)
            .error(R.drawable.ic_launcher_foreground)
            .fallback(R.drawable.ic_launcher_foreground)
            .into(itemView.findViewById(R.id.missionPatch))
    }

    private fun bindNameAndDate(resources: Resources, launch: Launch) {
        val missionName = if (launch.hasMissionName) launch.missionName else resources.getString(R.string.unknownMissionName)

        val launchDate = if (launch.hasDate) {
            resources.getString(R.string.launchDate, reformatAsDate(launch.date))
        } else {
            resources.getString(R.string.unknownLaunchDate)
        }

        itemView.findViewById<TextView>(R.id.missionName).text = missionName
        itemView.findViewById<TextView>(R.id.launchDate).text = launchDate
    }

    private fun bindOutcome(launch: Launch) {
        val textView = itemView.findViewById<TextView>(R.id.missionSuccessText)
        val imageView = itemView.findViewById<ImageView>(R.id.missionSuccessIcon)

        if (launch.hasSuccess) {
            val iconResId = if (launch.success) R.drawable.rocket else R.drawable.explosion
            textView.setText(R.string.missionOutcome)
            with(imageView) {
                setImageResource(iconResId)
                visibility = View.VISIBLE
            }
        } else {
            textView.setText(R.string.unknownMissionOutcome)
            with(imageView) {
                setImageResource(0)
                visibility = View.INVISIBLE
            }
        }
    }
}