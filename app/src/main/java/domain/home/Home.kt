package domain.home

import domain.home.devices.Device

class Home(val Address: HomeAddress) {

    private val _homeDevices: MutableList<Device> = mutableListOf()

    val homeDevices: List<Device>
        get() = _homeDevices.toList()

    fun addDevice(device: Device) {
        _homeDevices.add(device)
    }

    override fun hashCode(): Int = Address.hashCode()

    override fun toString(): String = "$Address ${_homeDevices.size}"

    override fun equals(other: Any?): Boolean =
        (other is Home) && other.Address == Address

}