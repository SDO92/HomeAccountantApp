package domain.home

import java.util.*

data class HomeId(val Id: UUID){
    companion object{
        fun Create(): HomeId{
            return  HomeId(UUID.randomUUID())
        }
    }

    fun toUUID():UUID = Id

}