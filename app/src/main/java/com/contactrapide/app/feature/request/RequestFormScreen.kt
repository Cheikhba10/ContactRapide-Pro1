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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.contactrapide.app.core.design.component.CrButton
import com.contactrapide.app.core.design.component.CrButtonStyle
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.OffWhite
import com.contactrapide.app.core.design.theme.TextDark
import com.contactrapide.app.core.design.theme.TextGray
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.IntentUtils
import com.contactrapide.app.feature.services.allServices

private val quartiers = listOf("Mermoz", "Point E", "Almadies", "Plateau", "Parcelles", "Yoff", "Ouakam", "Autre")
private val durees = listOf("Ponctuel", "Temps partiel", "Temps plein", "Long terme")

@Composable
fun RequestFormScreen(
    serviceIndex: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val service = allServices.getOrNull(serviceIndex)

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
            subtitle = service?.title ?: "Service",
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

                    Text("Vos coordonnees", color = Navy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    SimpleField(value = nom, onValueChange = { nom = it }, label = "Nom complet")
                    Spacer(Modifier.height(10.dp))
                    SimpleField(value = telephone, onValueChange = { telephone = it }, label = "Telephone")

                    Spacer(Modifier.height(18.dp))
                    Text("Votre besoin", color = Navy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    Text("Quartier", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    Chips(items = quartiers, selected = quartier, onSelect = { quartier = it }, cols = 3)
                    Spacer(Modifier.height(14.dp))

                    Text("Type de mission", color = TextGray, fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    Chips(items = durees, selected = duree, onSelect = { duree = it }, cols = 2)
                    Spacer(Modifier.height(14.dp))

                    SimpleField(value = budget, onValueChange = { budget = it }, label = "Budget mensuel (optionnel)")
                    Spacer(Modifier.height(10.dp))

                    SimpleField(value = details, onValueChange = { details = it }, label = "Details (optionnel)", multiLine = true)

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
                                erreur = "Nom et telephone obligatoires"
                            } else if (quartier.isBlank()) {
                                erreur = "Choisissez un quartier"
                            } else if (duree.isBlank()) {
                                erreur = "Choisissez un type de mission"
                            } else {
                                erreur = ""
                                val msg = buildString {
                                    appendLine("NOUVELLE DEMANDE - ContactRapide")
                                    appendLine()
                                    appendLine("Service : ${service?.title ?: "?"}")
                                    appendLine("Nom : $nom")
                                    appendLine("Telephone : $telephone")
                                    appendLine("Quartier : $quartier")
                                    appendLine("Mission : $duree")
                                    if (budget.isNotBlank()) appendLine("Budget : $budget FCFA")
                                    if (details.isNotBlank()) {
                                        appendLine()
                                        appendLine("Details :")
                                        appendLine(details)
                                    }
                                }
                                IntentUtils.whatsapp(context, msg)
                            }
                        },
                        style = CrButtonStyle.GREEN,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Vous serez mis en relation sous 1h ouvree.",
                        color = TextGray,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SimpleField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    multiLine: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = if (multiLine) Modifier.fillMaxWidth().height(110.dp) else Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = !multiLine,
        maxLines = if (multiLine) 4 else 1
    )
}

@Composable
private fun Chips(
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    cols: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.chunked(cols).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowItems.forEach { item ->
                    val isSel = item == selected
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSel) Navy else Navy.copy(alpha = 0.08f))
                            .clickable { onSelect(item) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = item,
                            color = if (isSel) White else TextDark,
                            fontSize = 13.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
