package infrastructure.homeaccountantsqlliterepo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import domain.home.Home
import domain.home.HomeAddress
import domain.home.devices.Device
import infrastructure.contracts.IHomeAccountantRepo
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "home_accountant_database"

class HomeAccountantRepo : IHomeAccountantRepo {

    private val database: HomeAccountantDatabase
    private val homeDao: HomeAccountantDao
    private val executor = Executors.newSingleThreadExecutor()

    companion object {
        private var INSTANCE: HomeAccountantRepo? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = HomeAccountantRepo(context)
            }
        }

        fun get(): HomeAccountantRepo = INSTANCE ?: throw IllegalStateException("HomeAccountantRepo must be initialized")
    }

    private constructor(context: Context) {

        database =
            Room.databaseBuilder(
                context.applicationContext,
                HomeAccountantDatabase::class.java,
                DATABASE_NAME
            ).build()

        homeDao = database.homeDao()
    }

    override fun getHomes(): LiveData<List<Home>> {
        var dbModels = homeDao.getHomes()
        var res: LiveData<List<Home>> =
            Transformations.map<List<HomeDbModel>, List<Home>>(dbModels) {
                it.map { dbModel -> mapTo(dbModel) }
            }
        return res;
    }

    override fun getHome(id: UUID): LiveData<Home?> {
        val dbModel = homeDao.getHome(id)
        return if (dbModel?.value != null) MutableLiveData<Home?>(mapTo(dbModel.value!!)) else MutableLiveData<Home?>(
            null
        )
    }

    override fun getHome(homeAddress: HomeAddress): LiveData<Home?> {
        val dbModel = homeDao.getHome(homeAddress.Address)
        return if (dbModel?.value != null) MutableLiveData<Home?>(mapTo(dbModel.value!!)) else MutableLiveData<Home?>(
            null
        )
    }

    override fun getDevices(homeAddress: HomeAddress): LiveData<List<Device>> {
        TODO("Not yet implemented")
    }

    override fun saveHome(homeAddress: HomeAddress) {
        executor.execute {
            homeDao.addNewHome(mapTo(homeAddress))
        }
    }

    override fun addDevice(home: Home, device: Device) {
        executor.execute {

        }

    }
}

private fun mapTo(homeAddress: HomeAddress): HomeDbModel {
    return HomeDbModel(Address = homeAddress.Address)
}

private fun mapTo(homeDbModel: HomeDbModel): Home {
    return Home(Address = HomeAddress(homeDbModel.Address))
}

private fun mapTo(device: Device, homeGuid: UUID): HomeDeviceDbModel{
    return HomeDeviceDbModel(device.Name, homeGuid)
}