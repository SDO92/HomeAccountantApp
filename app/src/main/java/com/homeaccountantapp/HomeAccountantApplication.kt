package com.homeaccountantapp

import android.app.Application
import infrastructure.homeaccountantsqlliterepo.HomeAccountantRepo

class HomeAccountantApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        HomeAccountantRepo.initialize(this)
    }
}