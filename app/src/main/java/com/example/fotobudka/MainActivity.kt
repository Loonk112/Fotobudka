package com.example.fotobudka

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
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


//                        lifecycleScope.launch {
//                            val pdfId = dataPdf()
//                            Toast.makeText(context, pdfId.await(), Toast.LENGTH_SHORT).show()
//                        }

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