package infrastructure.contracts

import androidx.lifecycle.LiveData
import domain.home.Home
import domain.home.HomeAddress
import domain.home.devices.Device
import java.util.*

interface IHomeAccountantRepo {

    fun getHomesLiveData(): LiveData<List<Home>>

    fun getHomeLiveData(id: UUID): LiveData<Home?>

    fun getHomeLiveData(homeAddress: HomeAddress): LiveData<Home?>

    fun getDevicesLiveData(homeAddress: HomeAddress): LiveData<List<Device>>

    fun createHome(homeAddress: HomeAddress)

    fun createHome(home: Home)

    fun addDevicesToHome(homeAddress: HomeAddress, devices: List<Device>)

}