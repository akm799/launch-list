package co.uk.akm.test.launchlistcr.data.db

import androidx.room.*
import co.uk.akm.test.launchlistcr.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistcr.data.entity.db.TimeStampEntity

@Dao
abstract class LaunchDao {
    private companion object {
        const val LAUNCH_ENTITY_NAME = "launch"
    }

    @Query("SELECT count(*) FROM timestamps WHERE entity_name = :entityName")
    abstract fun getTimeStampCount(entityName: String): Int

    @Query("SELECT cache_timestamp FROM timestamps WHERE entity_name = :entityName")
    abstract fun getTimeStamp(entityName: String): Long

    @Query("SELECT * FROM launches")
    abstract fun getLaunches(): List<LaunchDbEntity>

    @Query("DELETE FROM launches")
    abstract fun deleteLaunches(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLaunches(launches: List<LaunchDbEntity>)

    @Query("DELETE FROM timestamps WHERE entity_name = :entityName")
    abstract fun deleteTimeStamps(entityName: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTimeStamp(timestamp: TimeStampEntity)

    fun hasLaunchesCacheTime(): Boolean = (getTimeStampCount(LAUNCH_ENTITY_NAME) == 1)

    fun getLaunchesCacheTime(): Long = getTimeStamp(LAUNCH_ENTITY_NAME)

    @Transaction
    open fun cacheLaunches(launches: List<LaunchDbEntity>) {
        deleteLaunches()
        insertLaunches(launches)

        deleteTimeStamps(LAUNCH_ENTITY_NAME)
        insertTimeStamp(TimeStampEntity(LAUNCH_ENTITY_NAME, System.currentTimeMillis()))
    }
}