package domain.home.devices

class Device(val Name: String) {

    private val _deviceValues: MutableList<DeviceValue> = mutableListOf()

    val deviceValues get() = _deviceValues.toList()

    fun addValue(deviceValue: DeviceValue){
        _deviceValues.add(deviceValue)
    }

    override fun hashCode(): Int = Name.hashCode()

    override fun toString(): String = Name.toString()

    override fun equals(other: Any?): Boolean =
        (other is Device) && other.Name == Name

}