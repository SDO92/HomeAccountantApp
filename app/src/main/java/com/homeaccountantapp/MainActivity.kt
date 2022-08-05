package com.homeaccountantapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import domain.home.HomeAddress
import infrastructure.homeaccountantsqlliterepo.HomeAccountantRepo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crimeRepository =  HomeAccountantRepo.get()

        crimeRepository.saveHome(HomeAddress.CreateRandomAddress("MY_PREFIX"))
        crimeRepository.saveHome(HomeAddress.CreateRandomAddress("MY_PREFIX_1"))
        crimeRepository.saveHome(HomeAddress.CreateRandomAddress("MY_PREFIX_2"))


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