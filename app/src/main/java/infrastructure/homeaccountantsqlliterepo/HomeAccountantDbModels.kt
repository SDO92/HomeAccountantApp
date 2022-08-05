package infrastructure.homeaccountantsqlliterepo

import androidx.room.*
import java.util.*


internal const val DB_HOME_TABLE_NAME = "HOME_ACCOUNTANT_HOMES"
internal const val DB_HOME_DEVICES_TABLE_NAME = "HOME_DEVICES"


@Entity(tableName = DB_HOME_TABLE_NAME, indices = [Index(value = ["HOME_ROW_GUID"])])
internal data class
HomeDbModel(
    @PrimaryKey @ColumnInfo(name = "HOME_ADDRESS") val Address: String,
    @ColumnInfo(name = "HOME_ROW_GUID") val rowId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "ROW_DATE") val rowDate: Date = Date()
)

@Entity(
    tableName = DB_HOME_DEVICES_TABLE_NAME,
    indices = [Index(value = ["HOME_ROW_GUID", "DEVICE_ROW_GUID"])]
)
internal data class
HomeDeviceDbModel(
    @PrimaryKey @ColumnInfo(name = "DEVICE_NAME") val DeviceName: String,
    @ColumnInfo(name = "HOME_ROW_GUID") val homeRowId: UUID,
    @ColumnInfo(name = "DEVICE_ROW_GUID") val deviceRowId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "ROW_DATE") val rowDate: Date = Date()
)

internal data class HomeDevicesDbModel(
    @Embedded val home: HomeDbModel,
    @Relation(
        parentColumn = "HOME_ROW_GUID",
        entityColumn = "HOME_ROW_GUID"
    )
    val devices: List<HomeDeviceDbModel>
)