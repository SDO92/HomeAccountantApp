package infrastructure.contracts

import androidx.lifecycle.LiveData
import domain.home.Home
import domain.home.HomeAddress
import domain.home.devices.Device
import java.util.*

interface IHomeAccountantRepo {

    fun getHomes(): LiveData<List<Home>>

    fun getHome(id: UUID): LiveData<Home?>

    fun getHome(homeAddress: HomeAddress): LiveData<Home?>

    fun getDevices(homeAddress: HomeAddress): LiveData<List<Device>>

    fun saveHome(homeAddress: HomeAddress)

    fun addDevice(home: Home, device: Device)
}