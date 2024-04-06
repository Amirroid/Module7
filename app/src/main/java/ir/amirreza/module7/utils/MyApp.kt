package ir.amirreza.module7.utils

import android.app.Application
import ir.amirreza.module7.data.db.AppDatabase

class MyApp : Application() {
    override fun onCreate() {
        AppDatabase.init(this)
        super.onCreate()
    }
}