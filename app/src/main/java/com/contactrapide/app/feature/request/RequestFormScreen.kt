package com.contactrapide.app.feature.request

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrButton
import com.contactrapide.app.core.design.component.CrButtonStyle
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.IntentUtils

private val quartiers = listOf("Mermoz", "Point E", "Almadies", "Plateau", "Parcelles", "Yoff", "Ouakam", "Autre")
private val durees = listOf("Ponctuel", "Temps partiel", "Temps plein", "Long terme")

@Composable
fun RequestFormScreen(
    serviceName: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var nom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var quartier by remember { mutableStateOf("") }
    var duree by remember { mutableStateOf("") }
    var budget by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var erreur by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(OffWhite)) {
        CrTopBar(
            title = "Demande de service",
            subtitle = serviceName,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Text("Vos coordonnées", color = Navy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    CrTextField(value = nom, onValueChange = { nom = it }, label = "Nom complet *")
                    Spacer(Modifier.height(10.dp))
                    CrTextField(value = telephone, onValueChange = { telephone = it }, label = "Téléphone *", isPhone = true)

                    Spacer(Modifier.height(18.dp))
                    Text("Votre besoin", color = Navy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    // Quartier (chips)
                    Text("Quartier", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    ChipsRow(items = quartiers, selected = quartier, onSelect = { quartier = it }, columns = 3)
                    Spacer(Modifier.height(14.dp))

                    // Durée
                    Text("Type de mission", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    ChipsRow(items = durees, selected = duree, onSelect = { duree = it }, columns = 2)
                    Spacer(Modifier.height(14.dp))

                    CrTextField(value = budget, onValueChange = { budget = it }, label = "Budget mensuel (optionnel)", isPhone = true)
                    Spacer(Modifier.height(10.dp))

                    OutlinedTextField(
                        value = details,
                        onValueChange = { details = it },
                        label = { Text("Détails (nombre d'enfants, tâches, etc.)") },
                        modifier = Modifier.fillMaxWidth().height(110.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Navy,
                            unfocusedBorderColor = TextGray,
                            focusedLabelColor = Navy,
                            cursorColor = Navy
                        ),
                        maxLines = 4
                    )

                    if (erreur.isNotEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        Text(erreur, color = Color(0xFFC62828), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.height(20.dp))

                    CrButton(
                        text = "Envoyer via WhatsApp",
                        icon = "💬",
                        onClick = {
                            if (nom.isBlank() || telephone.isBlank()) {
                                erreur = "Nom et téléphone obligatoires"
                            } else if (quartier.isBlank()) {
                                erreur = "Choisissez un quartier"
                            } else if (duree.isBlank()) {
                                erreur = "Choisissez un type de mission"
                            } else {
                                erreur = ""
                                val message = buildString {
                                    appendLine("🔔 NOUVELLE DEMANDE — ContactRapide")
                                    appendLine()
                                    appendLine("📋 Service : $serviceName")
                                    appendLine("👤 Nom : $nom")
                                    appendLine("📞 Téléphone : $telephone")
                                    appendLine("📍 Quartier : $quartier")
                                    appendLine("⏱️ Mission : $duree")
                                    if (budget.isNotBlank()) appendLine("💰 Budget : $budget FCFA")
                                    if (details.isNotBlank()) {
                                        appendLine()
                                        appendLine("📝 Détails :")
                                        appendLine(details)
                                    }
                                }
                                IntentUtils.whatsapp(context, message)
                            }
                        },
                        style = CrButtonStyle.GREEN,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "💡 Vous serez mis en relation sous 1h ouvrée.",
                        color = TextGray,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CrTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPhone: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Navy,
            unfocusedBorderColor = TextGray,
            focusedLabelColor = Navy,
            cursorColor = Navy
        ),
        keyboardOptions = if (isPhone)
            androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone
            )
        else androidx.compose.foundation.text.KeyboardOptions.Default
    )
}

@Composable
private fun ChipsRow(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    columns: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.chunked(columns).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowItems.forEach { item ->
                    val isSelected = item == selected
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Navy else Navy.copy(alpha = 0.08f))
                            .clickable { onSelect(item) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = item,
                            color = if (isSelected) White else TextDark,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
