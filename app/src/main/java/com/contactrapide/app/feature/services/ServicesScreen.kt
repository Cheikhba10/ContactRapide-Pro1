package com.contactrapide.app.feature.services

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.IntentUtils

data class ServiceItem(
    val emoji: String,
    val title: String,
    val description: String,
    val whatsappMessage: String
)

private val services = listOf(
    ServiceItem(
        emoji = "🧹",
        title = "Ménage & Cuisine",
        description = "Femmes de ménage expérimentées, cuisinières qualifiées. Entretien maison, repassage, préparation de repas.",
        whatsappMessage = "Bonjour ContactRapide, je souhaite un service de Ménage & Cuisine."
    ),
    ServiceItem(
        emoji = "👶",
        title = "Garde d'enfants / Nounou",
        description = "Nounous douces et responsables. Garde à domicile, aide aux devoirs, surveillance des enfants.",
        whatsappMessage = "Bonjour ContactRapide, je souhaite un service de Garde d'enfants / Nounou."
    ),
    ServiceItem(
        emoji = "🚗",
        title = "Chauffeur & Sécurité",
        description = "Chauffeurs professionnels avec permis, gardiens de sécurité formés et fiables.",
        whatsappMessage = "Bonjour ContactRapide, je souhaite un service de Chauffeur & Sécurité."
    ),
    ServiceItem(
        emoji = "🤝",
        title = "Personnel polyvalent",
        description = "Aides à tout faire : courses, accompagnement, petits travaux, assistance quotidienne.",
        whatsappMessage = "Bonjour ContactRapide, je souhaite un service de Personnel polyvalent."
    )
)

@Composable
fun ServicesScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(
            title = "Nos Services",
            subtitle = "Personnel qualifié et vérifié",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            services.forEach { service ->
                ServiceCard(
                    service = service,
                    onClick = { IntentUtils.whatsapp(context, service.whatsappMessage) }
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ServiceCard(service: ServiceItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                    Text(service.emoji, fontSize = 34.sp)
                }
                Spacer(Modifier.width(14.dp))
                Text(
                    text = service.title,
                    color = Navy,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = service.description,
                color = TextGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "→ Demander ce service",
                color = Green,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
