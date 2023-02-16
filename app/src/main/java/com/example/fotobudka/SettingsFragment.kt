package com.example.fotobudka

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class SettingsFragment : Fragment() {

    private lateinit var timeInLayout: TextInputLayout
    private lateinit var timeIn: TextInputEditText
    private lateinit var countInLayout: TextInputLayout
    private lateinit var countIn: TextInputEditText
    private lateinit var nameInLayout: TextInputLayout
    private lateinit var nameIn: TextInputEditText
    private lateinit var colorsBtn: Button
    private lateinit var saveBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        timeInLayout = view.findViewById(R.id.delayInLayout)
        countInLayout = view.findViewById(R.id.countInLayout)
        nameInLayout = view.findViewById(R.id.nameInLayout)

        timeIn = view.findViewById(R.id.delayIn)
        countIn = view.findViewById(R.id.countIn)
        nameIn = view.findViewById(R.id.nameIn)

        colorsBtn = view.findViewById(R.id.ChangeColorsBtn)
        colorsBtn.setBackgroundColor(Color.rgb(Keeper.backR,Keeper.backG,Keeper.backB))
        colorsBtn.setTextColor(Color.rgb(Keeper.fontR,Keeper.fontG,Keeper.fontB))

        colorsBtn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_colorPicker)
        }

        timeIn.addTextChangedListener {
            if (it.toString() == "") {
                timeInLayout.error = "Delay between photos can not be empty"
            }
            else if (it.toString().toDouble() < 0.1) {
                timeInLayout.error = "Delay between photos must be over 100ms"
            }
            else {
                timeInLayout.error = null
            }
        }

        countIn.addTextChangedListener {
            if (it.toString() == "") {
                countInLayout.error = "Photos count can not be empty"
            }
            else if (it.toString().toInt() < 1) {
                countInLayout.error = "Photos count must be at least 1"
            }
            else {
                countInLayout.error = null
            }
        }

        nameIn.addTextChangedListener {
            if (it.toString() == "") {
                nameInLayout.error = "Banner name can not be empty"
            }
            else {
                nameInLayout.error = null
            }
        }

        saveBtn = view.findViewById(R.id.saveSettingsBtn)

        timeIn.setText("${Keeper.delay.toDouble()/1000}")
        countIn.setText("${Keeper.count}")
        nameIn.setText(Keeper.name)

        saveBtn.setOnClickListener {

            if(TextUtils.isEmpty(timeInLayout.error)&&TextUtils.isEmpty(countInLayout.error)&&TextUtils.isEmpty(nameInLayout.error)) {
                Keeper.delay = (timeIn.text.toString().toDouble()*1000).toInt()
                Keeper.count = (countIn.text.toString().toInt())
                Keeper.name = nameIn.text.toString()
                //TODO:...
                //Keeper.fontC = ...
                //Keeper.backC = ...
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_captureFragment)
            }
        }

        return view
    }
}