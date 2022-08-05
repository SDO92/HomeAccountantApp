package infrastructure.homeaccountantsqlliterepo

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
internal interface HomeAccountantDao {

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME")
    fun getHomes(): LiveData<List<HomeDbModel>>

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME WHERE HOME_ROW_GUID = (:id)")
    fun getHome(id: UUID): LiveData<HomeDbModel?>

    @Query("SELECT * FROM $DB_HOME_TABLE_NAME WHERE HOME_ADDRESS = (:homeAddress)")
    fun getHome(homeAddress: String): LiveData<HomeDbModel?>

    @Query("SELECT COUNT(1) FROM $DB_HOME_TABLE_NAME WHERE HOME_ROW_GUID = (:id)")
    fun isHomeExists(id: UUID): Int

    @Query("SELECT COUNT(1) FROM $DB_HOME_TABLE_NAME WHERE HOME_ADDRESS = (:homeAddress)")
    fun isHomeExists(homeAddress: String): Int

    @Insert
    fun addNewHome(dbModel: HomeDbModel)

    @Insert
    fun addNewHomeDevice(dbModel: HomeDeviceDbModel)

    @Transaction
    fun add(dbModelHome: HomeDbModel, dbModelDevice: HomeDeviceDbModel){
        addNewHome(dbModelHome)
        addNewHomeDevice(dbModelDevice)
    }

    @Update
    fun updateHome(dbModel: HomeDbModel)

    @Update
    fun updateHomeDevice(dbModel: HomeDeviceDbModel)



}