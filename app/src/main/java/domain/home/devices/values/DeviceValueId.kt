package domain.home.devices.values

import java.util.*

data class DeviceValueId(val value : UUID) {

    companion object{
        fun create(): DeviceValueId{
            return DeviceValueId(UUID.randomUUID())
        }
    }

    fun toUUID(): UUID = value

}