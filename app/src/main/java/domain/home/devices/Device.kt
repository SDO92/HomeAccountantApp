package domain.home.devices

import domain.home.devices.values.DeviceValue

class Device(val Id: DeviceId, val Name: String) {

    private val _deviceValues: MutableList<DeviceValue> = mutableListOf()

    val deviceValues get() = _deviceValues.toList()

    fun addValue(deviceValue: DeviceValue){
        _deviceValues.add(deviceValue)
    }

    companion object {

        fun createRandomDevice(deviceNamePrefix: String ): Device {
            return Device(DeviceId.create(), deviceNamePrefix + (0..999).random() )
        }

    }

}