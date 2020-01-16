package co.uk.akm.test.launchlistrx.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.uk.akm.test.launchlistrx.data.entity.db.LaunchDbEntity
import co.uk.akm.test.launchlistrx.data.entity.db.TimeStampEntity

@Database(entities = arrayOf(LaunchDbEntity::class, TimeStampEntity::class), version = 1)
abstract class LaunchDatabase : RoomDatabase() {
    companion object {
        private const val LAUNCHES_DB_NAME = "launches-db"

        @Volatile
        private var INSTANCE: LaunchDatabase? = null

        /**
         * Accepting Application instances to prevent the accidental passing of a non-application context.
         */
        fun singleInstance(app: Application): LaunchDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(app, LAUNCHES_DB_NAME).also { INSTANCE = it }
            }
        }

        private fun build(app: Application, dbName: String): LaunchDatabase {
            return Room.databaseBuilder(app, LaunchDatabase::class.java, dbName).build()
        }

        fun closeInstance() {
            if (INSTANCE?.isOpen == true) {
                INSTANCE?.close()
            }

            INSTANCE = null
        }
    }

    abstract fun launchDao(): LaunchDao
}