package com.contactrapide.app.feature.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Gold
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.AppConstants
import com.contactrapide.app.core.utils.IntentUtils

@Composable
fun AboutScreen(onBack: () -> Unit, onBecomeProvider: () -> Unit = {}) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(title = "À propos", onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Logo + nom
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp)
                    )Button(
    onClick = { onBecomeProvider() },
    modifier = Modifier.fillMaxWidth().height(56.dp),
    shape = RoundedCornerShape(14.dp),
    colors = ButtonDefaults.buttonColors(containerColor = Green)
) {
    Text("💼 Devenir prestataire", color = White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}

Spacer(Modifier.height(16.dp))
                    Spacer(Modifier.height(10.dp))
                    Text(
                        AppConstants.AGENCY_NAME,
                        color = Navy,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Besoin d'une bonne à Dakar ?",
                        color = Gold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Mission
            InfoCard(
                title = "Notre mission",
                body = "Nous mettons en relation les familles et entreprises de Dakar avec un personnel de confiance, rigoureusement sélectionné et vérifié.\n\nOn sélectionne ✅  On vérifie ✅  On place ✅"
            )

            // Horaires
            InfoCard(
                title = "Horaires d'ouverture",
                body = "Lundi - Vendredi : 08h00 - 20h00\nSamedi : 09h00 - 18h00\nDimanche : Sur rendez-vous"
            )

            // Contact
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "Nous joindre",
                        color = Navy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "📞  ${AppConstants.PHONE_DISPLAY}",
                        color = Navy,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { IntentUtils.call(context) }
                            .padding(vertical = 6.dp)
                    )
                    Text(
                        "💬  ${AppConstants.WHATSAPP_DISPLAY}",
                        color = Green,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { IntentUtils.whatsapp(context) }
                            .padding(vertical = 6.dp)
                    )
                    Text(
                        "📍  ${AppConstants.ADDRESS}",
                        color = TextDark,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { IntentUtils.openInGoogleMaps(context) }
                            .padding(vertical = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                AppConstants.SLOGAN,
                color = Navy,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(title, color = Navy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(body, color = TextGray, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}
