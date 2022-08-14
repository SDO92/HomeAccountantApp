package domain.home.devices

import java.util.*

data class DeviceId(val Id: UUID){
    companion object{
        fun create(): DeviceId{
            return  DeviceId(UUID.randomUUID())
        }
    }
}