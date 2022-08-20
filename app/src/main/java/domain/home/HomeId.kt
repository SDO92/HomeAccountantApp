package domain.home

import java.util.*

data class HomeId(val Id: UUID){
    companion object{
        fun create(): HomeId{
            return  HomeId(UUID.randomUUID())
        }
    }

    fun toUUID():UUID = Id

}