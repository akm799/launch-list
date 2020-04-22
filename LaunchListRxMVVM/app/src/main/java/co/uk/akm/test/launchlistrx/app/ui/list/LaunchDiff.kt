package co.uk.akm.test.launchlistrx.app.ui.list

import androidx.recyclerview.widget.DiffUtil
import co.uk.akm.test.launchlistrx.domain.model.Launch

val LAUNCH_DIFF = object : DiffUtil.ItemCallback<Launch>() {

    override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return (oldItem.flightNumber == newItem.flightNumber)
    }

    override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem.hasSameContentsAs(newItem)
    }
}

private fun Launch.hasSameContentsAs(other: Launch): Boolean {
    if (type != other.type) {
        return false
    }

    if (notSamePropertyAs(other, { hasMissionName }, { missionName } )) {
        return false
    }

    if (notSamePropertyAs(other, { hasDate }, { date } )) {
        return false
    }

    if (notSamePropertyAs(other, { hasSuccess }, { success } )) {
        return false
    }

    if (notSamePropertyAs(other, { hasMissionPatch }, { missionPatch } )) {
        return false
    }

    return true
}

private fun Launch.notSamePropertyAs(
    other: Launch,
    hasProperty: Launch.() -> Boolean,
    getProperty: Launch.() -> Any
): Boolean {
    return hasSamePropertyAs(other, hasProperty, getProperty).not()
}

private fun Launch.hasSamePropertyAs(
    other: Launch,
    hasProperty: Launch.() -> Boolean,
    getProperty: Launch.() -> Any
): Boolean {
    if (hasProperty(this)) {
        return (hasProperty(other) && getProperty(this) == getProperty(other))
    } else {
        return hasProperty(other).not()
    }
}
