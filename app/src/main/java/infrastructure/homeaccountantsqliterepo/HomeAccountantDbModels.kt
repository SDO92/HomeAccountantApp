package infrastructure.homeaccountantsqliterepo

import androidx.room.*
import java.util.*


internal const val DB_HOME_TABLE_NAME = "HOMES"
internal const val DB_HOME_DEVICE_TABLE_NAME = "HOME_DEVICE"
internal const val DB_HOME_DEVICES_TABLE_NAME = "HOME_DEVICES"
internal const val DB_HOME_DEVICES_VALUES_TABLE_NAME = "HOME_DEVICES_VALUES"

/*******************************************************************************/

internal class TableHome{
    companion object{
        const val HOME_TABLE_NAME = DB_HOME_TABLE_NAME
        const val HOME_ADDRESS = "HOME_ADDRESS"
        const val HOME_ROW_GUID = "HOME_ROW_GUID"
        const val HOME_ROW_DATE = "HOME_ROW_DATE"
    }
}

@Entity(
    tableName = TableHome.HOME_TABLE_NAME,
    indices = [Index(value = [TableHome.HOME_ADDRESS], unique = true)])
internal data class
HomeDbModel(
    @ColumnInfo(name = TableHome.HOME_ADDRESS) val Address: String,
    @PrimaryKey  @ColumnInfo(name = TableHome.HOME_ROW_GUID) val homeRowId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = TableHome.HOME_ROW_DATE) val rowDate: Date = Date()
)


/*******************************************************************************/


internal class TableHomeDevice{
    companion object{
        const val HOME_DEVICE_TABLE_NAME = DB_HOME_DEVICE_TABLE_NAME
        const val HOME_DEVICE_NAME = "DEVICE_NAME"
        const val HOME_DEVICE_ROW_GUID = "DEVICE_ROW_GUID"
        const val HOME_DEVICE_ROW_DATE = "DEVICE_ROW_DATE"
    }
}

@Entity(
    tableName = TableHomeDevice.HOME_DEVICE_TABLE_NAME,
    indices = [Index(value = [TableHomeDevice.HOME_DEVICE_NAME], unique = true)]
)
internal data class
HomeDeviceDbModel(
    @ColumnInfo(name = TableHomeDevice.HOME_DEVICE_NAME) val DeviceName: String,
    @PrimaryKey @ColumnInfo(name = TableHomeDevice.HOME_DEVICE_ROW_GUID) val deviceRowId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = TableHomeDevice.HOME_DEVICE_ROW_DATE) val rowDate: Date = Date()
)


/*******************************************************************************/


internal class TableHomeDevices{
    companion object{
        const val HOME_DEVICES_TABLE_NAME = DB_HOME_DEVICES_TABLE_NAME
        const val HOME_ROW_GUID = TableHome.HOME_ROW_GUID
        const val DEVICE_ROW_GUID = TableHomeDevice.HOME_DEVICE_ROW_GUID
        const val ROW_DATE = "ROW_DATE"
    }
}

@Entity(
    tableName = TableHomeDevices.HOME_DEVICES_TABLE_NAME,
    primaryKeys = [ TableHomeDevices.HOME_ROW_GUID, TableHomeDevices.DEVICE_ROW_GUID]
)
internal data class
OneHomeManyDevicesDbModel(
    @ColumnInfo(name = TableHomeDevices.HOME_ROW_GUID) val homeRowId: UUID ,
    @ColumnInfo(name = TableHomeDevices.DEVICE_ROW_GUID) val deviceRowId: UUID ,
    @ColumnInfo(name = TableHomeDevices.ROW_DATE) val rowDate: Date = Date()
)

internal data class HomeDevicesDbModel(
    @Embedded val home: HomeDbModel,
    @Relation(
        parentColumn = TableHomeDevices.HOME_ROW_GUID,
        entityColumn = TableHomeDevices.DEVICE_ROW_GUID,
        associateBy = Junction(OneHomeManyDevicesDbModel::class)
    )
    val devices: List<HomeDeviceDbModel>
){

}

/*******************************************************************************/

