package infrastructure.contracts

import androidx.lifecycle.LiveData
import domain.home.Home
import domain.home.HomeAddress
import domain.home.HomeId
import domain.home.devices.Device
import domain.home.devices.values.DeviceValue
import java.util.*

interface IHomeAccountantRepo {

    fun getHomeLiveData(homeId: HomeId): LiveData<Home?>

    fun getHomesLiveData(): LiveData<List<Home>>

    fun getHomeLiveData(id: UUID): LiveData<Home?>

    fun getHomeLiveData(homeAddress: HomeAddress): LiveData<Home?>

    fun getDevicesLiveData(homeAddress: HomeAddress): LiveData<List<Device>>

    fun createHome(home: Home)

    fun addDevicesToHome(homeAddress: HomeAddress, devices: List<Device>)

    fun addValueToDecice(device : Device, value: DeviceValue)

}