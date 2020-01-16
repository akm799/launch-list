package co.uk.akm.test.launchlistrx.presentation.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import co.uk.akm.test.launchlistrx.R
import co.uk.akm.test.launchlistrx.domain.model.Launch


class LaunchListAdapter : ListAdapter<Launch, LaunchViewHolder>(LAUNCH_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_launch, parent, false)

        return LaunchViewHolder(view)
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        holder.bindLaunch(getItem(position))
    }
}