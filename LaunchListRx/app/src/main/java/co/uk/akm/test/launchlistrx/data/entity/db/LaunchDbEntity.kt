package co.uk.akm.test.launchlistrx.data.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.uk.akm.test.launchlistrx.data.entity.LaunchEntity

@Entity(tableName = "launches")
data class LaunchDbEntity(

    @PrimaryKey
    @ColumnInfo(name = "flight_number")
    override val flight_number: Int?,

    @ColumnInfo(name = "mission_name")
    override val mission_name: String?,

    @ColumnInfo(name = "launch_date_unix")
    override val launch_date_unix: Long?,

    @ColumnInfo(name = "launch_date_local")
    override val launch_date_local: String?,

    @ColumnInfo(name = "launch_success")
    override val launch_success: Boolean?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @ColumnInfo(name = "mission_patch")
    override val mission_patch: String?,

    @ColumnInfo(name = "mission_patch_small")
    override val mission_patch_small: String?

) : LaunchEntity {

    constructor(entity: LaunchEntity) : this(
        entity.flight_number,
        entity.mission_name,
        entity.launch_date_unix,
        entity.launch_date_local,
        entity.launch_success,
        entity.type,
        entity.mission_patch,
        entity.mission_patch_small
    )
}