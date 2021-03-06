package co.uk.akm.test.launchlistrx.data.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timestamps")
data class TimeStampEntity(

    @PrimaryKey
    @ColumnInfo(name = "entity_name")
    val entity_name: String,

    @ColumnInfo(name = "cache_timestamp")
    val cache_timestamp: Long
)