package com.example.fotobudka

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fotobudka.room.AppDb
import com.example.fotobudka.room.Settings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var appDb: AppDb
    lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDb = AppDb.getDatabase(this)
        test()
        setContentView(R.layout.activity_main)
        getPermissions()


        Keeper.delay = settings.delay
        Keeper.count = settings.count
        Keeper.name = settings.bannerName
        Keeper.setFontFromString(settings.bannerFontColor)
        Keeper.setBackgroundFromString(settings.bannerBGColor)

    }

    private fun getPermissions() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                getPermissions()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun test(){
        GlobalScope.launch {
            coroutineContext.run {
                val tmp = appDb.SettingsDao().getSettingsById(0)
                if(tmp == null){
                    val id = 0
                    val delay = Keeper.delay
                    val count = Keeper.count
                    val name = Keeper.name
                    val bgColor = Keeper.getBackHex()
                    val foColor = Keeper.getFontHex()
                    settings = Settings(id,delay,count,name,bgColor,foColor)
                    appDb.SettingsDao().insert(settings)
                }else{
                    settings = tmp
                }
            }
        }
    }

}