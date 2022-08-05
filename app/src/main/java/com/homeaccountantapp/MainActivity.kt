package com.homeaccountantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import domain.home.HomeAddress
import domain.home.devices.Device
import infrastructure.homeaccountantsqlliterepo.HomeAccountantRepo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crimeRepository =  HomeAccountantRepo.get()

        var addr_1 = HomeAddress.CreateRandomAddress("MY_PREFIX")
        var addr_2 = HomeAddress.CreateRandomAddress("MY_PREFIX_1")
        var addr_3 = HomeAddress.CreateRandomAddress("MY_PREFIX_2")


        crimeRepository.createHome(addr_1)
        crimeRepository.createHome(addr_2)
        crimeRepository.createHome(addr_3)

        crimeRepository.addDevicesToHome(addr_1, listOf(Device("Dev_1"), Device("DEV_2"), Device("DEV_3")))
        crimeRepository.addDevicesToHome(addr_2, listOf(Device("Dev_4"), Device("DEV_5"), Device("DEV_6")))
        crimeRepository.addDevicesToHome(addr_3, listOf(Device("Dev_7"), Device("DEV_8"), Device("DEV_9")))

        var homeList = crimeRepository.getHomes()

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