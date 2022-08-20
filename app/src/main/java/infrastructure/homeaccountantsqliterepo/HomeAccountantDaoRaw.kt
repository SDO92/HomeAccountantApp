package infrastructure.homeaccountantsqliterepo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import java.util.*


@Dao
internal interface IHomeAccountantDaoRaw {
    @RawQuery(observedEntities = [FullAggregateRowDbModel::class])
    fun getHome(query: SupportSQLiteQuery): LiveData<List<FullAggregateRowDbModel>>
}

internal class HomeAccountantDaoRaw(private val rawDao: IHomeAccountantDaoRaw){

    fun getHome(id: UUID): LiveData<List<FullAggregateRowDbModel>> {

        val sql = "select H.*, D.*, V.* from ${HomeDbModel.TABLE_NAME} H " +
                "left join ${OneHomeManyDevicesDbModel.TABLE_NAME} HD on HD.${OneHomeManyDevicesDbModel.HOME_ROW_GUID} = H.${HomeDbModel.HOME_ROW_GUID}  " +
                "left join ${HomeDeviceDbModel.TABLE_NAME} D on D.${HomeDeviceDbModel.HOME_DEVICE_ROW_GUID} = HD.${OneHomeManyDevicesDbModel.DEVICE_ROW_GUID} " +
                "left join ${OneDeviceManyValuesDbModel.TABLE_NAME} DV on DV.${OneDeviceManyValuesDbModel.DEVICE_ROW_GUID} = D.${HomeDeviceDbModel.HOME_DEVICE_ROW_GUID} " +
                "left join ${HomeDeviceValueDbmodel.TABLE_NAME} V on V.${HomeDeviceValueDbmodel.VALUE_ROW_GUID} = DV.${OneDeviceManyValuesDbModel.VALUE_ROW_GUID} " +
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

