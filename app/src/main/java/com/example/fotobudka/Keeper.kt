package com.example.fotobudka

object Keeper {
    var delay = 600
    var count = 3
    var name = "Banner"
    var fontR = 255
    var fontG = 255
    var fontB = 255
    var backR = 150
    var backG = 150
    var backB = 150

    fun setFontFromString(string: String){
        fontR = string.substring(1,3).toInt(16)
        fontG = string.substring(3,5).toInt(16)
        fontB = string.substring(5,7).toInt(16)
    }

    fun setBackgroundFromString(string: String){
        backR = string.substring(1,3).toInt(16)
        backG = string.substring(3,5).toInt(16)
        backB = string.substring(5,7).toInt(16)
    }

    fun getFontHex() : String {
        return String.format("#%02x%02x%02x", fontR, fontG, fontB)
    }

    fun getBackHex() : String {
        return String.format("#%02x%02x%02x", backR, backG, backB)
    }
}