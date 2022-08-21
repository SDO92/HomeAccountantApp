package infrastructure.homeaccountantsqliterepo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import domain.home.Home
import domain.home.HomeAddress
import domain.home.HomeId
import domain.home.devices.Device
import domain.home.devices.DeviceId
import domain.home.devices.values.DeviceValue
import domain.home.devices.values.DeviceValueId
import java.util.*


@Dao
internal interface IHomeAccountantDaoRaw {
    @RawQuery(observedEntities = [FullAggregateRowDbModel::class])
    fun getHome(query: SupportSQLiteQuery): LiveData<List<FullAggregateRowDbModel>>
}

internal class HomeAccountantDaoRaw(private val rawDao: IHomeAccountantDaoRaw){

    fun getHomeLiveData(id: UUID): LiveData<List<FullAggregateRowDbModel>> {

        val sql = "select H.*, D.*, V.* from ${HomeDbModel.TABLE_NAME} H " + System.lineSeparator() +
                "left join ${OneHomeManyDevicesDbModel.TABLE_NAME} HD on HD.${OneHomeManyDevicesDbModel.HOME_ROW_GUID} = H.${HomeDbModel.HOME_ROW_GUID}  " + System.lineSeparator() +
                "left join ${HomeDeviceDbModel.TABLE_NAME} D on D.${HomeDeviceDbModel.HOME_DEVICE_ROW_GUID} = HD.${OneHomeManyDevicesDbModel.DEVICE_ROW_GUID} " + System.lineSeparator() +
                "left join ${OneDeviceManyValuesDbModel.TABLE_NAME} DV on DV.${OneDeviceManyValuesDbModel.DEVICE_ROW_GUID} = D.${HomeDeviceDbModel.HOME_DEVICE_ROW_GUID} " + System.lineSeparator() +
                "left join ${HomeDeviceValueDbmodel.TABLE_NAME} V on V.${HomeDeviceValueDbmodel.VALUE_ROW_GUID} = DV.${OneDeviceManyValuesDbModel.VALUE_ROW_GUID} " + System.lineSeparator() +
                "where H.${HomeDbModel.HOME_ROW_GUID} = ?";

        return rawDao.getHome(query = SimpleSQLiteQuery(sql, arrayOf( id.toString())));
    }

}

internal data class FullAggregateRowDbModel(
    @Embedded
    val home : HomeDbModel,
    @Embedded
    val device: HomeDeviceDbModel?,
    @Embedded
    val value: HomeDeviceValueDbmodel?
)

internal fun mapTo(list: List<FullAggregateRowDbModel>) : Map<HomeDbModel, Map<HomeDeviceDbModel, List<HomeDeviceValueDbmodel>>>{

    var res =  mutableMapOf<HomeDbModel, MutableMap<HomeDeviceDbModel, MutableList<HomeDeviceValueDbmodel>>>()

    if (list.isEmpty())
        return res

    var homes: Map<HomeDbModel, List<FullAggregateRowDbModel>> = list.groupBy {it.home}

    for ( home in homes){
        res[home.key] = mutableMapOf<HomeDeviceDbModel,  MutableList<HomeDeviceValueDbmodel>>()
        var devices: Map<HomeDeviceDbModel?, List<FullAggregateRowDbModel>> = home.value.groupBy { it.device }

        for (device in devices)
        {
            if (device?.key == null)
                continue
            val isContains : Boolean = res[home.key]?.containsKey(device.key!!) ?: true
            if (!isContains){
                res[home.key]?.put(device.key!!, mutableListOf())
            }

            var values: List<HomeDeviceValueDbmodel?> = device.value.map { it.value }
            for (value in values){
                if (value == null)
                    continue
                res[home.key]?.get(device.key)?.add(value)
            }
        }
    }

    return res;
}

internal fun mapTo(list: Map<HomeDbModel, Map<HomeDeviceDbModel, List<HomeDeviceValueDbmodel>>>): List<Home> {

    var res = mutableListOf<Home>()

    for(home in list.keys){
        var h = Home(Id = HomeId(home.homeRowId), HomeAddress(home.Address))
        for (device in list[home]?.keys ?: emptySet()){
            var d = Device(Id = DeviceId(device.deviceRowId), Name = device.DeviceName )
            h.addDevice(d)
            for (deviceValue: HomeDeviceValueDbmodel in list[home]?.get(device)!!)
            {
                val v = DeviceValue(Id = DeviceValueId(deviceValue.valueRowId), Value = deviceValue.value )
                d.addValue(v)
            }
        }
        res.add(h)
    }
    return res;
}

