package co.uk.akm.test.launchlistcr.helper

import co.uk.akm.test.launchlistcr.BuildConfig
import co.uk.akm.test.launchlistcr.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistcr.data.entity.server.LaunchApiEntity
import co.uk.akm.test.launchlistcr.data.entity.server.RocketEntity
import co.uk.akm.test.launchlistcr.domain.model.Launch
import co.uk.akm.test.launchlistcr.domain.model.impl.LaunchData

fun apiEntities(vararg flightNumbers: Int): List<LaunchApiEntity> = flightNumbers.map { apiEntity(it) }

private fun apiEntity(flightNumber: Int): LaunchApiEntity {
    val rocket = RocketEntity(BuildConfig.LAUNCH_TYPE)

    return LaunchApiEntity(flightNumber, null, null, null, rocket, null, null)
}

fun dbEntities(vararg flightNumbers: Int): List<LaunchDbEntity> = flightNumbers.map { dbEntity(it) }

private fun dbEntity(flightNumber: Int): LaunchDbEntity {
    return LaunchDbEntity(flightNumber, null, null, null, null, BuildConfig.LAUNCH_TYPE, null, null)
}

fun models(vararg flightNumbers: Int): List<Launch> = flightNumbers.map { model(it) }

private fun model(flightNumber: Int): Launch {
    return LaunchData(flightNumber, BuildConfig.LAUNCH_TYPE, null, null, null, null)
}