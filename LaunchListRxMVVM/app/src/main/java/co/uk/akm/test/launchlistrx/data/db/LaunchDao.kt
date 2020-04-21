package co.uk.akm.test.launchlistrx.data.db

import androidx.room.*
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.entity.db.TimeStampEntity
import io.reactivex.Single

@Dao
abstract class LaunchDao {
    private companion object {
        const val LAUNCH_LIST_ENTITY_NAME = "launch_list"
        const val LAUNCH_DETAILS_ENTITY_NAME_PREFIX = "launch_details_"
    }

    @Query("SELECT count(*) FROM timestamps WHERE entity_name = :entityName")
    abstract fun getTimeStampCount(entityName: String): Single<Int>

    @Query("SELECT cache_timestamp FROM timestamps WHERE entity_name = :entityName")
    abstract fun getTimeStamp(entityName: String): Single<Long>

    @Query("SELECT * FROM launches")
    abstract fun getLaunches(): Single<List<LaunchDbEntity>>

    @Query("DELETE FROM launches")
    abstract fun deleteLaunches(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLaunches(launches: List<LaunchDbEntity>) // Cannot return anything from here due to a room-rxjava2 bug.

    @Query("DELETE FROM timestamps WHERE entity_name = :entityName")
    abstract fun deleteTimeStamps(entityName: String): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTimeStamp(timestamp: TimeStampEntity) // Cannot return anything from here due to a room-rxjava2 bug.

    fun hasLaunchesCacheTime(): Single<Boolean> = getTimeStampCount(LAUNCH_LIST_ENTITY_NAME).map { it == 1 }

    fun getLaunchesCacheTime(): Single<Long> = getTimeStamp(LAUNCH_LIST_ENTITY_NAME)

    @Transaction
    open fun cacheLaunches(launches: List<LaunchDbEntity>) {
        deleteLaunches()
        insertLaunches(launches)

        deleteTimeStamps(LAUNCH_LIST_ENTITY_NAME)
        insertTimeStamp(TimeStampEntity(LAUNCH_LIST_ENTITY_NAME, System.currentTimeMillis()))
    }
}