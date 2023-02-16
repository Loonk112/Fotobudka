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

    //TODO: Hexy

    fun getFontHex() : String {
        return String.format("#%02x%02x%02x", fontR, fontG, fontB)
    }

    fun getBackHex() : String {
        return String.format("#%02x%02x%02x", backR, backG, backB)
    }
}