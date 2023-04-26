package ie.wit.shoppingapp.main

import android.app.Application
import ie.wit.shoppingapp.models.StoreJSONStore
//import ie.wit.shoppingapp.models.StoreManager
import ie.wit.shoppingapp.models.StoreStore
import timber.log.Timber

class StoreApp: Application() {
    // lateinit var stores: StoreManager
    lateinit var stores: StoreStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        stores = StoreManager
      //  stores = StoreJSONStore(this.applicationContext)
        StoreJSONStore.initialize(this.applicationContext)

        Timber.i("Store App started ")
    }
}