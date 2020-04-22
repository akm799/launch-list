package co.uk.akm.test.launchlistrx.data.db

import androidx.room.*
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.entity.db.TimeStampEntity
import io.reactivex.Single

@Dao
abstract class LaunchDao {
    private companion object {
        const val LAUNCH_LIST_CACHE_ID = "launch_list"
    }

    @Query("SELECT count(*) FROM timestamps WHERE id_int = :idInt")
    abstract fun getTimeStampCount(idInt: Int): Single<Int>

    @Query("SELECT count(*) FROM timestamps WHERE id_string = :idString")
    abstract fun getTimeStampCount(idString: String): Single<Int>

    @Query("SELECT cache_timestamp FROM timestamps WHERE id_int = :idInt")
    abstract fun getTimeStamp(idInt: Int): Single<Long>

    @Query("SELECT cache_timestamp FROM timestamps WHERE id_string = :idString")
    abstract fun getTimeStamp(idString: String): Single<Long>

    @Query("SELECT * FROM launches")
    abstract fun getLaunches(): Single<List<LaunchDbEntity>>

    @Query("DELETE FROM launches")
    abstract fun deleteLaunches(): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLaunches(launches: List<LaunchDbEntity>) // Cannot return anything from here due to a room-rxjava2 bug.

    @Query("DELETE FROM timestamps WHERE id_int = :idInt")
    abstract fun deleteTimeStamps(idInt: Int): Single<Int>

    @Query("DELETE FROM timestamps WHERE id_string = :idString")
    abstract fun deleteTimeStamps(idString: String): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTimeStamp(timestamp: TimeStampEntity) // Cannot return anything from here due to a room-rxjava2 bug.

    fun hasLaunchesCacheTime(): Single<Boolean> = getTimeStampCount(LAUNCH_LIST_CACHE_ID).map { it == 1 }

    fun getLaunchesCacheTime(): Single<Long> = getTimeStamp(LAUNCH_LIST_CACHE_ID)

    @Transaction
    open fun cacheLaunches(launches: List<LaunchDbEntity>) {
        deleteLaunches()
        insertLaunches(launches)

        deleteTimeStamps(LAUNCH_LIST_CACHE_ID)
        insertTimeStamp(TimeStampEntity(id_int = null, id_string = LAUNCH_LIST_CACHE_ID, cache_timestamp = System.currentTimeMillis()))
    }
}