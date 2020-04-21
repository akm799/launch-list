package co.uk.akm.test.launchlistrx.data.entity

import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity


interface LaunchDetailsEntity : LaunchEntity {

    val details: String?

    val site_name_long: String?
}