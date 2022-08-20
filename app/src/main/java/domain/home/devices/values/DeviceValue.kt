package domain.home.devices.values

data class DeviceValue(val Id: DeviceValueId, val Value: String){

    companion object{
        fun createRandom() : DeviceValue {
            return DeviceValue(DeviceValueId.create(), "")
        }
    }

}