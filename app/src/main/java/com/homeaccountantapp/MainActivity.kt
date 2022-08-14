package com.homeaccountantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import domain.home.Home
import domain.home.HomeAddress
import domain.home.devices.Device
import infrastructure.homeaccountantsqliterepo.HomeAccountantRepo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crimeRepository =  HomeAccountantRepo.get()

        var addr_1 = HomeAddress.createRandomAddress("MY_PREFIX")
        var addr_2 = HomeAddress.createRandomAddress("MY_PREFIX_1")
        var addr_3 = HomeAddress.createRandomAddress("MY_PREFIX_2")


        crimeRepository.createHome(Home.createNew(addr_1))
        crimeRepository.createHome(Home.createNew(addr_2))
        crimeRepository.createHome(Home.createNew(addr_3))

        crimeRepository.addDevicesToHome(addr_1,
            listOf<Device>(
                Device.createRandomDevice("Dev_1"),
                Device.createRandomDevice("DEV_2"),
                Device.createRandomDevice("DEV_3")))
        crimeRepository.addDevicesToHome(addr_2,
            listOf(
                Device.createRandomDevice("Dev_4"),
                Device.createRandomDevice("DEV_5"),
                Device.createRandomDevice("DEV_6")))
        crimeRepository.addDevicesToHome(addr_3,
            listOf(
                Device.createRandomDevice("Dev_7"),
                Device.createRandomDevice("DEV_8"),
                Device.createRandomDevice("DEV_9")))

        var homeList = crimeRepository.getHomesLiveData()

        homeList.observe(
            this,
            Observer { crimes ->
                crimes?.let {
                    Log.i("TEST_TAG", "Got records ${crimes.size}")
                    for (item in it){
                        Log.i("TEST_TAG", "Home $item")
                    }
                }
            })
    }

}