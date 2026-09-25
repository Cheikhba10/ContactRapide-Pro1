package com.contactrapide.app.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.R
import com.contactrapide.app.core.design.component.CrActionCard
import com.contactrapide.app.core.design.theme.Gold
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.AppConstants
import com.contactrapide.app.core.utils.IntentUtils

@Composable
fun HomeScreen(
    onCall: () -> Unit,
    onWhatsApp: () -> Unit,
    onLocation: () -> Unit,
    onServices: () -> Unit,
    onAbout: () -> Unit,
    onContact: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // === HEADER ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "ContactRapide",
                modifier = Modifier.size(90.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "ContactRapide",
                color = White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Besoin d'une bonne à Dakar ?",
                color = Gold,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "On sélectionne. On vérifie. On place.",
                color = White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }

        // === BOUTONS PRINCIPAUX ===
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CrActionCard(
                emoji = "📞",
                title = "Appeler",
                subtitle = AppConstants.PHONE_DISPLAY,
                backgroundColor = Navy,
                textColor = White,
                onClick = {
                    IntentUtils.call(context)
                    onCall()
                }
            )

            CrActionCard(
                emoji = "💬",
                title = "WhatsApp",
                subtitle = AppConstants.WHATSAPP_DISPLAY,
                backgroundColor = Green,
                textColor = White,
                onClick = {
                    IntentUtils.whatsapp(context)
                    onWhatsApp()
                }
            )

            CrActionCard(
                emoji = "📍",
                title = "Localisation",
                subtitle = "Parcelles rond-point / Agence Yas",
                backgroundColor = White,
                textColor = Navy,
                onClick = onLocation
            )

            CrActionCard(
                emoji = "📋",
                title = "Services",
                subtitle = "Ménage, Nounou, Chauffeur, Polyvalent",
                backgroundColor = Gold,
                textColor = Navy,
                onClick = onServices
            )

            CrActionCard(
                emoji = "ℹ️",
                title = "À propos",
                subtitle = "Informations & horaires",
                backgroundColor = White,
                textColor = Navy,
                onClick = onAbout
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = AppConstants.SLOGAN,
                color = Navy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}
