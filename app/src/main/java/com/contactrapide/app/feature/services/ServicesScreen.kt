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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White

data class ServiceItem(
    val emoji: String,
    val title: String,
    val location: String,
    val priceFrom: String,
    val tags: List<String>,
    val description: String,
    val gradient: List<Color>
)

val allServices = listOf(
    ServiceItem(
        emoji = "🧹",
        title = "Aide menagere",
        location = "Dakar et banlieue",
        priceFrom = "5 000 FCFA",
        tags = listOf("Menage", "Cuisine", "Repassage", "Surfaces"),
        description = "Femmes de menage experimentees et verifiees. Entretien complet, cuisine, repassage.",
        gradient = listOf(Color(0xFF16A085), Color(0xFF1ABC9C))
    ),
    ServiceItem(
        emoji = "👶",
        title = "Nounou / Garde d'enfants",
        location = "Dakar et banlieue",
        priceFrom = "6 000 FCFA",
        tags = listOf("Garde a domicile", "Sortie d'ecole", "Aide devoirs"),
        description = "Nounous douces et responsables. Garde a domicile, sortie d'ecole, aide aux devoirs.",
        gradient = listOf(Color(0xFFE67E22), Color(0xFFF39C12))
    ),
    ServiceItem(
        emoji = "🚗",
        title = "Chauffeur et Securite",
        location = "Dakar et banlieue",
        priceFrom = "10 000 FCFA",
        tags = listOf("Permis B", "Vehicule", "Garde de nuit", "Surveillance"),
        description = "Chauffeurs professionnels et gardiens formes. Permis verifies, references controlees.",
        gradient = listOf(Color(0xFF8E44AD), Color(0xFF9B59B6))
    ),
    ServiceItem(
        emoji = "🤝",
        title = "Personnel polyvalent",
        location = "Dakar et banlieue",
        priceFrom = "5 000 FCFA",
        tags = listOf("Courses", "Accompagnement", "Petits travaux"),
        description = "Aides a tout faire : courses, accompagnement, petits travaux, assistance quotidienne.",
        gradient = listOf(Color(0xFF2980B9), Color(0xFF3498DB))
    )
)

@Composable
fun ServicesScreen(
    onBack: () -> Unit,
    onServiceClick: (Int) -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(
            title = "Nos Services",
            subtitle = "Personnel qualifie et verifie",
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Decouvrez nos categories de personnel. Chaque profil est selectionne et verifie avant placement.",
                color = TextGray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            allServices.forEachIndexed { index, service ->
                ServiceCard(
                    service = service,
                    onClick = { onServiceClick(index) }
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
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Brush.linearGradient(service.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Text(service.emoji, fontSize = 64.sp)
            }

            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = service.title,
                    color = Navy,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = service.location,
                    color = TextGray,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Tarif indicatif : a partir de",
                    color = TextGray,
                    fontSize = 12.sp
                )
                Text(
                    text = service.priceFrom,
                    color = Green,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(12.dp))

                service.tags.chunked(2).forEach { rowTags ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        rowTags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Navy.copy(alpha = 0.08f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = tag,
                                    color = Navy,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = service.description,
                    color = TextDark,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Spacer(Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Green)
                        .clickable { onClick() }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Demander ce service",
                        color = White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
