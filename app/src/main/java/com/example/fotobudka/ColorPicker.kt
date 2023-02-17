package com.example.fotobudka

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.fotobudka.room.AppDb
import com.example.fotobudka.room.Settings
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ColorPicker : Fragment() {

    private lateinit var appDb: AppDb
    private lateinit var exampleView: TextView
    private lateinit var backgroundRValView: TextView
    private lateinit var backgroundRSlider: SeekBar
    private lateinit var backgroundGValView: TextView
    private lateinit var backgroundGSlider: SeekBar
    private lateinit var backgroundBValView: TextView
    private lateinit var backgroundBSlider: SeekBar
    private lateinit var fontRValView: TextView
    private lateinit var fontRSlider: SeekBar
    private lateinit var fontGValView: TextView
    private lateinit var fontGSlider: SeekBar
    private lateinit var fontBValView: TextView
    private lateinit var fontBSlider: SeekBar
    private lateinit var saveBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_color_picker, container, false)

        appDb = AppDb.getDatabase(view.context)
        exampleView = view.findViewById(R.id.exampleTextView)
        backgroundRValView = view.findViewById(R.id.backRVal)
        backgroundRSlider = view.findViewById(R.id.backRIn)
        backgroundGValView = view.findViewById(R.id.backGVal)
        backgroundGSlider = view.findViewById(R.id.backGIn)
        backgroundBValView = view.findViewById(R.id.backBVal)
        backgroundBSlider = view.findViewById(R.id.backBIn)
        fontRValView = view.findViewById(R.id.fontRVal)
        fontRSlider = view.findViewById(R.id.fontRIn)
        fontGValView = view.findViewById(R.id.fontGVal)
        fontGSlider = view.findViewById(R.id.fontGIn)
        fontBValView = view.findViewById(R.id.fontBVal)
        fontBSlider = view.findViewById(R.id.fontBIn)
        saveBtn = view.findViewById(R.id.saveColorsBtn)

        saveBtn.setOnClickListener {
            saveSettings()
            Navigation.findNavController(view).navigate(R.id.action_colorPicker_to_settingsFragment)
        }

        backgroundRValView.text = Keeper.backR.toString()
        backgroundRSlider.max = 255
        backgroundRSlider.progress = Keeper.backR
        backgroundGValView.text = Keeper.backG.toString()
        backgroundGSlider.max = 255
        backgroundGSlider.progress = Keeper.backG
        backgroundBValView.text = Keeper.backB.toString()
        backgroundBSlider.max = 255
        backgroundBSlider.progress = Keeper.backB
        fontRValView.text = Keeper.fontR.toString()
        fontRSlider.max = 255
        fontRSlider.progress = Keeper.fontR
        fontGValView.text = Keeper.fontG.toString()
        fontGSlider.max = 255
        fontGSlider.progress = Keeper.fontG
        fontBValView.text = Keeper.fontB.toString()
        fontBSlider.max = 255
        fontBSlider.progress = Keeper.fontB


        setExample()

        backgroundRSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Keeper.backR = progress
                backgroundRValView.text = progress.toString()
                setExample()
            }
        })
        backgroundGSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Keeper.backG = progress
                backgroundGValView.text = progress.toString()
                setExample()
            }
        })
        backgroundBSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Keeper.backB = progress
                backgroundBValView.text = progress.toString()
                setExample()
            }
        })
        fontRSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Keeper.fontR = progress
                fontRValView.text = progress.toString()
                setExample()
            }
        })
        fontGSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Keeper.fontG = progress
                fontGValView.text = progress.toString()
                setExample()
            }
        })
        fontBSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Keeper.fontB = progress
                fontBValView.text = progress.toString()
                setExample()
            }
        })

        return view
    }

    fun setExample() {
        exampleView.setBackgroundColor(Color.rgb(backgroundRSlider.progress,backgroundGSlider.progress,backgroundBSlider.progress))
        exampleView.setTextColor(Color.rgb(fontRSlider.progress,fontGSlider.progress,fontBSlider.progress))
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveSettings() {
        GlobalScope.launch {
            coroutineContext.run {
                val id = 0
                val delay = Keeper.delay
                val count = Keeper.count
                val name = Keeper.name
                val bgColor = Keeper.getBackHex()
                val foColor = Keeper.getFontHex()
                appDb.SettingsDao().insert(Settings(id,delay,count,name,bgColor,foColor))
            }
        }
    }

}