package com.example.fotobudka
import android.widget.Toast
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import android.content.Context
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

data class Pdf(
    val images: ArrayList<String>,
    val banerName: String,
    val banerBgColor: String,
    val banerFontColor: String
)

fun dataPdf(
//            images: ArrayList<String>,
//            banerName: String,
//            banerBgColor: String,
//            banerFontColor: String
           ): Deferred<String> =GlobalScope.async {

//    utworzenie klasy jest wymagane do deserializacji


//przykladowy obiekt
    val objectToSend: Pdf = Pdf(
        arrayListOf(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=",
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII="
        ), "testowy", "#fff", "#000"
    )

        val (_, response, result) =
            "https://photoapi-production.up.railway.app/pdf".httpPost()
                .jsonBody(Gson().toJson(objectToSend).toString())
                .responseString()


    return@async result.toString()
}


