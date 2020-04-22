package co.uk.akm.test.launchlistrx.data.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timestamps")
data class TimeStampEntity(

    /**
     * Dummy primary key to satisfy the entity requirements.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key")
    val key: Int = 0,

    @ColumnInfo(name = "id_int")
    val id_int: Int?,

    @ColumnInfo(name = "id_string")
    val id_string: String?,

    @ColumnInfo(name = "cache_timestamp")
    val cache_timestamp: Long
)