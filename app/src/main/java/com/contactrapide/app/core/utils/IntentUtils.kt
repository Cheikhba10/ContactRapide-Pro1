package com.contactrapide.app.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object AppConstants {
    const val PHONE_NUMBER = "761301330"
    const val WHATSAPP_NUMBER = "221761301330"
    const val PHONE_DISPLAY = "76 130 13 30"
    const val WHATSAPP_DISPLAY = "+221 76 130 13 30"
    const val LATITUDE = 14.7551443
    const val LONGITUDE = -17.4306665
    const val ADDRESS = "Parcelles rond-point / Agence Yas, Dakar"
    const val AGENCY_NAME = "ContactRapide"
    const val SLOGAN = "La tranquillité d'esprit n'a pas de prix"
}

object IntentUtils {

    fun call(context: Context, phone: String = AppConstants.PHONE_NUMBER) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        context.startActivity(intent)
    }

    fun whatsapp(context: Context, message: String? = null, number: String = AppConstants.WHATSAPP_NUMBER) {
        val url = if (message.isNullOrBlank()) {
            "https://wa.me/$number"
        } else {
            "https://wa.me/$number?text=${Uri.encode(message)}"
        }
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            // Fallback navigateur
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    fun openInGoogleMaps(context: Context) {
        val lat = AppConstants.LATITUDE
        val lon = AppConstants.LONGITUDE
        val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(${AppConstants.AGENCY_NAME})")
        try {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback navigateur
            context.startActivity(
                Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=$lat,$lon"))
            )
        }
    }

    fun shareApp(context: Context) {
        val text = "Découvrez ContactRapide — l'agence de personnel de confiance à Dakar.\n" +
                "📞 ${AppConstants.PHONE_DISPLAY}"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Partager ContactRapide"))
    }
}
