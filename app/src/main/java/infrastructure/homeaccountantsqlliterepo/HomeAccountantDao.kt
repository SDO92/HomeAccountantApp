package infrastructure.homeaccountantsqlliterepo

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
internal interface HomeAccountantDao {

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME")
    fun getHomes(): LiveData<List<HomeDbModel>>

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME H left join $DB_HOME_DEVICES_TABLE_NAME D on H.HOME_ROW_GUID = D.HOME_ROW_GUID")
    fun getHomesWithDevices(): LiveData<Map<HomeDbModel,List<HomeDeviceDbModel>?>>

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME WHERE HOME_ROW_GUID = (:id)")
    fun getHome(id: UUID): LiveData<HomeDbModel?>

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME WHERE HOME_ADDRESS = (:homeAddress)")
    fun getHome(homeAddress: String): LiveData<HomeDbModel?>

    @Query("SELECT COUNT(1) FROM $DB_HOME_TABLE_NAME WHERE HOME_ROW_GUID = (:id)")
    fun isHomeExists(id: UUID): Int

    @Query("SELECT COUNT(1) FROM $DB_HOME_TABLE_NAME WHERE HOME_ADDRESS = (:homeAddress)")
    fun isHomeExists(homeAddress: String): Int

    @Query("SELECT * FROM $DB_HOME_DEVICES_TABLE_NAME WHERE HOME_ROW_GUID = (:homeRowId) and DEVICE_NAME = (:deviceName)")
    fun getDevice(homeRowId: UUID, deviceName: String): HomeDeviceDbModel?

    @Query("SELECT * FROM $DB_HOME_DEVICES_TABLE_NAME WHERE HOME_ROW_GUID = (:homeRowId)")
    fun getDevices(homeRowId: UUID): List<HomeDeviceDbModel>

    @Query("SELECT HOME_ROW_GUID FROM $DB_HOME_TABLE_NAME WHERE HOME_ADDRESS = (:homeAddress)")
    fun getHomeGuid(homeAddress: String): UUID?

    @Query("SELECT DEVICE_ROW_GUID FROM $DB_HOME_DEVICES_TABLE_NAME WHERE HOME_ROW_GUID = (:homeRowId) and DEVICE_NAME = (:deviceName)")
    fun getDeviceGuid(homeRowId: UUID, deviceName: String): UUID?


    @Insert
    fun addNewHome(dbModel: HomeDbModel)

    @Insert
    fun addNewHomeDevice(dbModel: HomeDeviceDbModel)

    @Transaction
    fun addDeviceToHome(dbModelHome: HomeDbModel, dbModelDevice: HomeDeviceDbModel){
        addNewHome(dbModelHome)
        addNewHomeDevice(dbModelDevice)
    }

    @Update
    fun updateHome(dbModel: HomeDbModel)

    @Update
    fun updateHomeDevice(dbModel: HomeDeviceDbModel)



}