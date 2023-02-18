package com.example.fotobudka
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

data class Pdf(
    val images: ArrayList<String>,
    val banerName: String,
    val banerBgColor: String,
    val banerFontColor: String
)

fun sendHttp(
            images: ArrayList<String>,
            banerName: String,
            banerBgColor: String,
            banerFontColor: String
           ): Deferred<String> =GlobalScope.async {

//    utworzenie klasy jest wymagane do deserializacji
    val objectToSend: Pdf = Pdf(
        images, banerName, banerBgColor, banerFontColor
    )

        val (_, response, result) =
            "https://photoapi-production.up.railway.app/pdf".httpPost()
                .jsonBody(Gson().toJson(objectToSend).toString())
                .responseString()

        var (id,error)=result
        id=id.toString()
        id=id.drop(1)
        id=id.dropLast(1)

    return@async id
}


