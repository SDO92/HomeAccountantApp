package infrastructure.homeaccountantsqliterepo

import androidx.room.*
import java.util.*

/*******************************************************************************/

@Entity(
    tableName = HomeDbModel.TABLE_NAME,
    indices = [Index(value = [HomeDbModel.HOME_ADDRESS], unique = true)])
internal data class HomeDbModel(
    @PrimaryKey @ColumnInfo(name = HomeDbModel.HOME_ROW_GUID) val homeRowId: UUID,
    @ColumnInfo(name = HomeDbModel.HOME_ADDRESS) val Address: String,
    @ColumnInfo(name = HomeDbModel.HOME_ROW_DATE) val rowDate: Date = Date()
){
    companion object{
        const val TABLE_NAME = "HOMES"
        const val HOME_ADDRESS = "HOME_ADDRESS"
        const val HOME_ROW_GUID = "HOME_ROW_GUID"
        const val HOME_ROW_DATE = "HOME_ROW_DATE"
    }
}

/*******************************************************************************/

@Entity(
    tableName = HomeDeviceDbModel.TABLE_NAME,
    indices = [Index(value = [HomeDeviceDbModel.HOME_DEVICE_NAME], unique = true)]
)
internal data class
HomeDeviceDbModel(
    @PrimaryKey @ColumnInfo(name = HomeDeviceDbModel.HOME_DEVICE_ROW_GUID) val deviceRowId: UUID = UUID.randomUUID(),
    @ColumnInfo(name = HomeDeviceDbModel.HOME_DEVICE_NAME) val DeviceName: String,
    @ColumnInfo(name = HomeDeviceDbModel.HOME_DEVICE_ROW_DATE) val rowDate: Date = Date()
){
    companion object{
        const val TABLE_NAME = "HOME_DEVICE"
        const val HOME_DEVICE_NAME = "DEVICE_NAME"
        const val HOME_DEVICE_ROW_GUID = "DEVICE_ROW_GUID"
        const val HOME_DEVICE_ROW_DATE = "DEVICE_ROW_DATE"
    }
}

/*******************************************************************************/

@Entity(
    tableName = OneHomeManyDevicesDbModel.TABLE_NAME,
    primaryKeys = [ OneHomeManyDevicesDbModel.HOME_ROW_GUID, OneHomeManyDevicesDbModel.DEVICE_ROW_GUID]
)
internal data class
OneHomeManyDevicesDbModel(
    @ColumnInfo(name = OneHomeManyDevicesDbModel.HOME_ROW_GUID) val homeRowId: UUID ,
    @ColumnInfo(name = OneHomeManyDevicesDbModel.DEVICE_ROW_GUID) val deviceRowId: UUID ,
    @ColumnInfo(name = OneHomeManyDevicesDbModel.ROW_DATE) val rowDate: Date = Date()
){
    companion object{
        const val TABLE_NAME = "HOME_DEVICES"
        const val HOME_ROW_GUID = HomeDbModel.HOME_ROW_GUID
        const val DEVICE_ROW_GUID = HomeDeviceDbModel.HOME_DEVICE_ROW_GUID
        const val ROW_DATE = "ROW_DATE"
    }
}

internal data class HomeDevicesDbModel(
    @Embedded val home: HomeDbModel,
    @Relation(
        parentColumn = OneHomeManyDevicesDbModel.HOME_ROW_GUID,
        entityColumn = OneHomeManyDevicesDbModel.DEVICE_ROW_GUID,
        associateBy = Junction(OneHomeManyDevicesDbModel::class)
    )
    val devices: List<HomeDeviceDbModel>
){

}

/*******************************************************************************/


@Entity(
    tableName = OneDeviceManyValuesDbModel.TABLE_NAME,
    primaryKeys = [ OneDeviceManyValuesDbModel.DEVICE_ROW_GUID, OneDeviceManyValuesDbModel.VALUE_ROW_GUID]
)
internal data class
OneDeviceManyValuesDbModel(
    @ColumnInfo(name = OneDeviceManyValuesDbModel.DEVICE_ROW_GUID) val deviceRowId: UUID ,
    @ColumnInfo(name = OneDeviceManyValuesDbModel.VALUE_ROW_GUID) val valueRowId: UUID ,
    @ColumnInfo(name = OneDeviceManyValuesDbModel.ROW_DATE) val rowDate: Date = Date()
){
    companion object{
        const val TABLE_NAME = "HOME_DEVICE_VALUES"
        const val DEVICE_ROW_GUID = HomeDeviceDbModel.HOME_DEVICE_ROW_GUID
        const val VALUE_ROW_GUID =  HomeDeviceValueDbmodel.VALUE_ROW_GUID
        const val ROW_DATE = "ROW_DATE"
    }
}

internal data class HomeDeviceValuesDbModel(
    @Embedded val device: HomeDeviceDbModel,
    @Relation(
        parentColumn = HomeDeviceDbModel.HOME_DEVICE_ROW_GUID,
        entityColumn = HomeDeviceValueDbmodel.VALUE_ROW_GUID,
        associateBy = Junction(OneDeviceManyValuesDbModel::class)
    )
    val values: List<HomeDeviceValueDbmodel>
){

}

@Entity(
    tableName = HomeDeviceValueDbmodel.TABLE_NAME,
    primaryKeys = [ HomeDeviceValueDbmodel.VALUE_ROW_GUID]
)
internal data class HomeDeviceValueDbmodel(
    @ColumnInfo(name = HomeDeviceValueDbmodel.VALUE_ROW_GUID) val valueRowId: UUID ,
    @ColumnInfo(name = HomeDeviceValueDbmodel.VALUE) val value: String ,
    @ColumnInfo(name = HomeDeviceValueDbmodel.ROW_DATE) val rowDate: Date = Date())
{
    companion object{
        const val TABLE_NAME = "HOME_DEVICE_VALUE"
        const val VALUE_ROW_GUID = "HOME_DEVICE_VALUE_ROW_GUID"
        const val VALUE = "DEVICE_VALUE"
        const val ROW_DATE = "ROW_DATE"
    }
}

internal data class HomeWithDevicesWithValues(
    @Embedded val home: HomeDbModel,
    @Relation(
        entity = HomeDeviceDbModel::class,
        parentColumn = OneDeviceManyValuesDbModel.DEVICE_ROW_GUID,
        entityColumn = OneDeviceManyValuesDbModel.VALUE_ROW_GUID
    )
    val devices: List<HomeDeviceValuesDbModel>
)
{}

/*******************************************************************************/
