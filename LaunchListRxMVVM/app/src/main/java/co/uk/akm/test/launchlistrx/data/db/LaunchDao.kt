package co.uk.akm.test.launchlistrx.data.db

import androidx.room.*
import co.uk.akm.test.launchlistrx.data.entity.LaunchDetailsEntity
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDetailsDbEntity
import co.uk.akm.test.launchlistrx.data.entity.db.TimeStampEntity
import io.reactivex.Single

@Dao
abstract class LaunchDao {
    private companion object {
        const val LAUNCH_LIST_CACHE_ID = "launch_list"
        const val LAUNCH_DETAILS_NAME_CACHE_ID = "launch_list"
    }

    @Query("SELECT count(*) FROM timestamps WHERE id_string = :idString")
    abstract fun getTimeStampCount(idString: String): Single<Int>

    @Query("SELECT count(*) FROM timestamps WHERE id_string = :idString AND id_int = :idInt")
    abstract fun getTimeStampCount(idString: String, idInt: Int): Single<Int>

    @Query("SELECT cache_timestamp FROM timestamps WHERE id_string = :idString")
    abstract fun getTimeStamp(idString: String): Single<Long>

    @Query("SELECT cache_timestamp FROM timestamps WHERE id_string = :idString AND id_int = :idInt")
    abstract fun getTimeStamp(idString: String, idInt: Int): Single<Long>

    @Query("SELECT * FROM launches")
    abstract fun getLaunches(): Single<List<LaunchDbEntity>>

    @Query("SELECT * FROM launch_details WHERE flight_number = :flightNumber")
    abstract fun getLaunchDetails(flightNumber: Int): Single<LaunchDetailsDbEntity>

    @Query("DELETE FROM launches")
    abstract fun deleteLaunches(): Single<Int>

    @Query("DELETE FROM launch_details WHERE flight_number = :flightNumber")
    abstract fun deleteLaunchDetails(flightNumber: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLaunches(launches: List<LaunchDbEntity>) // Cannot return anything from here due to a room-rxjava2 bug.

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLaunchDetails(launchDetails: LaunchDetailsDbEntity) // Cannot return anything from here due to a room-rxjava2 bug.

    @Query("DELETE FROM timestamps WHERE id_string = :idString")
    abstract fun deleteTimeStamps(idString: String): Single<Int>

    @Query("DELETE FROM timestamps WHERE id_string = :idString AND id_int = :idInt")
    abstract fun deleteTimeStamp(idString: String, idInt: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertTimeStamp(timestamp: TimeStampEntity) // Cannot return anything from here due to a room-rxjava2 bug.

    fun hasLaunchesCacheTime(): Single<Boolean> = getTimeStampCount(LAUNCH_LIST_CACHE_ID).map { it == 1 }

    fun getLaunchesCacheTime(): Single<Long> = getTimeStamp(LAUNCH_LIST_CACHE_ID)

    fun hasLaunchDetailsCacheTime(flightNumber: Int): Single<Boolean> = getTimeStampCount(LAUNCH_DETAILS_NAME_CACHE_ID, flightNumber).map { it == 1 }

    fun getLaunchDetailsCacheTime(flightNumber: Int): Single<Long> = getTimeStamp(LAUNCH_DETAILS_NAME_CACHE_ID, flightNumber)

    @Transaction
    open fun cacheLaunches(launches: List<LaunchDbEntity>) {
        deleteLaunches()
        insertLaunches(launches)

        deleteTimeStamps(LAUNCH_LIST_CACHE_ID)
        insertTimeStamp(TimeStampEntity(id_int = null, id_string = LAUNCH_LIST_CACHE_ID, cache_timestamp = System.currentTimeMillis()))
    }

    @Transaction
    open fun cacheLaunchDetails(launchDetails: LaunchDetailsDbEntity) {
        launchDetails.flight_number?.let { flightNumber ->
            deleteLaunchDetails(flightNumber)
            insertLaunchDetails(launchDetails)

            deleteTimeStamp(LAUNCH_DETAILS_NAME_CACHE_ID, flightNumber)
            insertTimeStamp(TimeStampEntity(id_int = flightNumber, id_string = LAUNCH_DETAILS_NAME_CACHE_ID, cache_timestamp = System.currentTimeMillis()))
        }
    }
}