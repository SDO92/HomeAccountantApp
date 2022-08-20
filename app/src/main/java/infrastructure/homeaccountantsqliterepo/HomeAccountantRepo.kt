package infrastructure.homeaccountantsqliterepo

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import androidx.room.RoomDatabase
import domain.home.Home
import domain.home.HomeAddress
import domain.home.HomeId
import domain.home.devices.Device
import domain.home.devices.DeviceId
import domain.home.devices.values.DeviceValue
import infrastructure.contracts.IHomeAccountantRepo
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "home_accountant_database"

class HomeAccountantRepo : IHomeAccountantRepo {

    private val database: HomeAccountantDatabase
    private val homeDao: HomeAccountantDao
    private val homeRawDaw: HomeAccountantDaoRaw
    private val executor = Executors.newSingleThreadExecutor()

    companion object {
        private var INSTANCE: HomeAccountantRepo? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = HomeAccountantRepo(context)
            }
        }

        fun get(): HomeAccountantRepo =
            INSTANCE ?: throw IllegalStateException("HomeAccountantRepo must be initialized")
    }

    private constructor(context: Context) {

        var builder = Room.databaseBuilder(
            context.applicationContext,
            HomeAccountantDatabase::class.java,
            DATABASE_NAME
        )

        builder.setQueryCallback(RoomDatabase.QueryCallback { sqlQuery, bindArgs ->
            Log.i("DB_TAG", "\r\nSQL Query: $sqlQuery \r\n SQL Args: $bindArgs")
        }, Executors.newSingleThreadExecutor())

        database = builder.build()
        homeDao = database.homeDao()
        homeRawDaw = database.homeRawDao()
    }

    override fun getHomesLiveData(): LiveData<List<Home>> {
        var x = homeDao.getHomesWithDevicesLiveData();
        var r: LiveData<List<Home>> =
            Transformations.map(x) {
                it.map { y -> mapTo(y) }
            }
        return r;
    }

    override fun getHomeLiveData(homeId: HomeId): LiveData<Home?> {
        var dbModels = homeRawDaw.getHome(homeId.toUUID())



        TODO("Not yet implemented")
    }

    override fun getHomeLiveData(id: UUID): LiveData<Home?> {
        val dbModel = homeDao.getHomeLiveData(id)
        return if (dbModel.value != null) MutableLiveData<Home?>(mapTo(dbModel.value!!)) else MutableLiveData<Home?>(
            null
        )
    }

    override fun getHomeLiveData(homeAddress: HomeAddress): LiveData<Home?> {
        val dbModel = homeDao.getHomeLiveData(homeAddress.Address)
        return if (dbModel.value != null) MutableLiveData<Home?>(mapTo(dbModel.value!!)) else MutableLiveData<Home?>(
            null
        )
    }

    override fun getDevicesLiveData(homeAddress: HomeAddress): LiveData<List<Device>> {
        var homeGuid: UUID? =
            homeDao.getHomeGuid(homeAddress.Address) ?: return MutableLiveData(listOf())

        var dbModels: List<HomeDevicesDbModel> = homeDao.getDevices(homeGuid!!)
        var res = dbModels.flatMap { x -> x.devices.map { y -> mapTo(y) } }
        return MutableLiveData(res);
    }

    override fun createHome(home: Home) {
        executor.execute {
            val isExists = homeDao.isHomeExists(home.Address.Address) > 0
            if (!isExists) {
                homeDao.addNewHome(mapTo(home))
            }
        }
    }

    override fun addDevicesToHome(homeAddress: HomeAddress, devices: List<Device>) {
        executor.execute {
            val dbHome = homeDao.getHome(homeAddress.Address)
            val isExists = dbHome != null
            if (!isExists)
                return@execute
            for (item in devices)
            {
                var x = mapTo(item)
                var y = OneHomeManyDevicesDbModel(dbHome!!.home.homeRowId, x.deviceRowId)
                homeDao.addDeviceToHome(y,x)
            }

        }
    }

    override fun addValueToDecice(device: Device, value: DeviceValue) {
        executor.execute {
            homeDao.addValueToDevice(deviceId = device.Id.toUUID(), mapTo(value))
        }
    }

}

internal fun mapTo(home: Home): HomeDbModel {
    return HomeDbModel(Address = home.Address.Address, homeRowId = home.Id.toUUID() )
}

internal fun mapTo(homeDbModel: HomeDbModel): Home {
    return Home(Id = HomeId(homeDbModel.homeRowId), Address = HomeAddress(homeDbModel.Address))
}

internal fun mapTo(device: Device): HomeDeviceDbModel {
    return HomeDeviceDbModel(deviceRowId = device.Id.Id, DeviceName = device.Name )
}

internal fun mapTo(deviceDbModel: HomeDeviceDbModel): Device {
    return Device(DeviceId(deviceDbModel.deviceRowId), deviceDbModel.DeviceName)
}

internal fun mapTo(x: HomeDevicesDbModel): Home {

    var res = mapTo(x.home)
    var devices = x.devices
    for (device in devices) {

        var dev = mapTo(device)
        res.addDevice(dev)
    }

    return res
}

internal fun mapTo(value: DeviceValue): HomeDeviceValueDbmodel {
    return HomeDeviceValueDbmodel(valueRowId = value.Id.toUUID(),value = value.Value )
}

