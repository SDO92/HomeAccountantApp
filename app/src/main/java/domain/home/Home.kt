package domain.home

import domain.home.devices.Device

class Home(val Id : HomeId, val Address: HomeAddress) {

    private val _homeDevices: MutableList<Device> = mutableListOf()

    val homeDevices: List<Device>
        get() = _homeDevices.toList()

    fun addDevice(device: Device) {
        _homeDevices.add(device)
    }

    companion object{

        fun createNew(address: HomeAddress) : Home {
                return Home(HomeId.Create(),address )
        }

    }

}