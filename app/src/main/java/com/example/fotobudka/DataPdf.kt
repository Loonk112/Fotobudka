package com.example.fotobudka
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

fun DataPdf(images: ArrayList<String>,
            banerName: String,
            banerBgColor: String,
            banerFontColor: String){

//    utworzenie klasy jest wymagane do deserializacji
    data class Pdf(
        val images: ArrayList<String>,
        val banerName: String,
        val banerBgColor: String,
        val banerFontColor: String
    )

//przykladowy obiekt
    val objectToSend: Pdf = Pdf(
        arrayListOf(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=",
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
        ), "testowy", "#fff", "#fff"
    )

    //request
    val (_, _, result) = "https://photoapi-production.up.railway.app/pdf".httpPost()
        .jsonBody(Gson().toJson(objectToSend).toString())
        .responseString()
    println(result)
}


