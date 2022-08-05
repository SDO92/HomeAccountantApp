package domain.users

import domain.home.Home

class AppUser(val UserId: UserId) {

    private val _homeList: MutableList<Home> = mutableListOf()

    val homeList
        get() = _homeList.toList()

    fun addHome(home: Home){
        _homeList.add(home)
    }

}