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
        var x = homeDao.getHomesWithDevices();

        //<LiveData<Map<HomeDbModel,List<HomeDeviceDbModel>>>, List<Home>>
        var r : LiveData<List<Home>> =
            Transformations.map(x) {
                  mapTo(it)
            }
/*
        var res: LiveData<List<Home>> =
            Transformations.map<List<HomeDbModel>, List<Home>>(dbModels) {
                it.map { dbModel -> mapTo(dbModel) }
            }*/
        return r;
    }

    override fun getHome(id: UUID): LiveData<Home?> {
        val dbModel = homeDao.getHome(id)
        return if (dbModel?.value != null) MutableLiveData<Home?>(mapTo(dbModel.value!!)) else MutableLiveData<Home?>(null)
    }

    override fun getHome(homeAddress: HomeAddress): LiveData<Home?> {
        val dbModel = homeDao.getHome(homeAddress.Address)
        return if (dbModel?.value != null) MutableLiveData<Home?>(mapTo(dbModel.value!!)) else MutableLiveData<Home?>(null)
    }

    override fun getDevices(homeAddress: HomeAddress): LiveData<List<Device>> {
        var homeGuid: UUID? = homeDao.getHomeGuid(homeAddress.Address) ?: return MutableLiveData()

        var dbModels = homeDao.getDevices(homeGuid!!)
        return MutableLiveData(dbModels.map { mapTo(it) });
    }

    override fun createHome(homeAddress: HomeAddress) {
        executor.execute {
            val isExists = homeDao.isHomeExists(homeAddress.Address) > 0
            if (!isExists){
                homeDao.addNewHome(mapTo(homeAddress))
            }
        }
    }

    override fun createHome(home: Home) {
        executor.execute {
            val isExists = homeDao.isHomeExists(home.Address.Address) > 0
            if (!isExists){
                homeDao.addNewHome(mapTo(home.Address))
            }
        }
    }

    override fun addDevicesToHome(homeAddress: HomeAddress, devices: List<Device>) {
        executor.execute {
            val dbHome = homeDao.getHome(homeAddress.Address).value
            val isExists = dbHome != null
            if (!isExists)
                return@execute
            for (item in devices)
                homeDao.addNewHomeDevice(mapTo(dbHome!!.rowId, item))
        }
    }


}

private fun mapTo(homeAddress: HomeAddress): HomeDbModel {
    return HomeDbModel(Address = homeAddress.Address)
}

private fun mapTo(home: Home): HomeDbModel {
    return HomeDbModel(Address = home.Address.Address)
}

private fun mapTo(homeDbModel: HomeDbModel): Home {
    return Home(Address = HomeAddress(homeDbModel.Address))
}

private fun mapTo(home: Home, dbModel: HomeDbModel): HomeDbModel {
    return HomeDbModel(Address = home.Address.Address, rowId = dbModel.rowId)
}

private fun mapTo(homeGuid: UUID, device: Device): HomeDeviceDbModel{
    return HomeDeviceDbModel(device.Name, homeGuid)
}

private fun mapTo(deviceDbModel : HomeDeviceDbModel) : Device{
    return Device(deviceDbModel.DeviceName)
}

private fun mapTo(x : Map<HomeDbModel,List<HomeDeviceDbModel>?>) : List<Home>{

    var res: MutableList<Home> = mutableListOf()

    for (key in x.keys){
        var home = mapTo(key)
        var devices = x[key]
        if (devices != null){
        for (device in devices){
            var dev = mapTo(device)
            home.addDevice(dev)
        }
        }
        res.add(home)
    }

    return res
}