package infrastructure.homeaccountantsqliterepo

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
internal interface HomeAccountantDao {

    @Transaction
    @Query("SELECT * FROM ${HomeDbModel.TABLE_NAME} ")
    fun getHomesWithDevices(): List<HomeDevicesDbModel>

    @Transaction
    @Query("SELECT * FROM ${HomeDbModel.TABLE_NAME} ")
    fun getHomesWithDevicesLiveData(): LiveData<List<HomeDevicesDbModel>>

    @Transaction
    @Query("SELECT * FROM  ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ROW_GUID} = (:id)")
    fun getHome(id: UUID): HomeDevicesDbModel?

    @Transaction
    @Query("SELECT * FROM  ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ROW_GUID} = (:id)")
    fun getHomeLiveData(id: UUID): LiveData<HomeDevicesDbModel?>

    @Transaction
    @Query("SELECT * FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ADDRESS} = (:homeAddress)")
    fun getHome(homeAddress: String): HomeDevicesDbModel?

    @Transaction
    @Query("SELECT * FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ADDRESS} = (:homeAddress)")
    fun getHomeLiveData(homeAddress: String): LiveData<HomeDevicesDbModel?>


    @Query("SELECT COUNT(1) FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ROW_GUID} = (:id)")
    fun isHomeExists(id: UUID): Int

    @Query("SELECT COUNT(1) FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ADDRESS} = (:homeAddress)")
    fun isHomeExists(homeAddress: String): Int


    @Query("SELECT ${HomeDbModel.HOME_ROW_GUID} FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ADDRESS} = (:homeAddress)")
    fun getHomeGuid(homeAddress: String): UUID?

    @Query("SELECT D.${HomeDeviceDbModel.HOME_DEVICE_ROW_GUID} FROM  " +
            "${HomeDbModel.TABLE_NAME} H " +
            "join ${OneHomeManyDevicesDbModel.TABLE_NAME} HD on H.${HomeDbModel.HOME_ROW_GUID} = HD.${OneHomeManyDevicesDbModel.HOME_ROW_GUID} " +
            "join ${HomeDeviceDbModel.TABLE_NAME} D on HD.${OneHomeManyDevicesDbModel.DEVICE_ROW_GUID} = D.${HomeDeviceDbModel.HOME_DEVICE_ROW_GUID} " +
            " where H.${HomeDbModel.HOME_ROW_GUID} = (:homeRowId) and  D.${HomeDeviceDbModel.HOME_DEVICE_NAME} = (:deviceName)"
    )
    fun getDeviceGuid(homeRowId: UUID, deviceName: String): UUID?

    @Query("SELECT * FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ROW_GUID} = (:homeRowId)")
    fun getDevices(homeRowId: UUID): List<HomeDevicesDbModel>

    @Query("SELECT * FROM ${HomeDbModel.TABLE_NAME} WHERE ${HomeDbModel.HOME_ROW_GUID} = (:homeRowId)")
    fun getDevicesLiveData(homeRowId: UUID): LiveData<List<HomeDevicesDbModel>>


    @Insert
    fun addNewHome(dbModel: HomeDbModel)

    @Insert
    fun addNewHomeDevice(dbModel: HomeDeviceDbModel)

    @Insert
    fun addNewHomeDevices(dbModel: OneHomeManyDevicesDbModel)

    @Transaction
    fun addDeviceToHome(dbModel1: OneHomeManyDevicesDbModel, dbModel2: HomeDeviceDbModel ){
        addNewHomeDevices(dbModel1)
        addNewHomeDevice(dbModel2)
    }

    @Update
    fun updateHome(dbModel: HomeDbModel)

    @Update
    fun updateHomeDevice(dbModel: HomeDeviceDbModel)



}