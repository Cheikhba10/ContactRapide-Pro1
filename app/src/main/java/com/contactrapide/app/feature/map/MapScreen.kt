package com.contactrapide.app.feature.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.contactrapide.app.core.design.component.CrTopBar
import com.contactrapide.app.core.design.theme.Gold
import com.contactrapide.app.core.design.theme.Green
import com.contactrapide.app.core.design.theme.Navy
import com.contactrapide.app.core.design.theme.NavyDark
import com.contactrapide.app.core.design.theme.White
import com.contactrapide.app.core.utils.AppConstants
import com.contactrapide.app.core.utils.IntentUtils
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

// Satellite Esri World Imagery (URL CORRIGEE)
private val SATELLITE_SOURCE = XYTileSource(
    "EsriWorldImagery",
    1, 19, 256, ".jpg",
    arrayOf("https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}")
)

enum class MapMode(val label: String) {
    PLAN("Plan"),
    SATELLITE("Satellite")
}

// Points de repere (juste 3 pour ne pas surcharger)
private data class Landmark(
    val name: String,
    val lat: Double,
    val lon: Double
)

private val landmarks = listOf(
    Landmark("Rond-point Parcelles", 14.7543000, -17.4300000),
    Landmark("Station Total", 14.7560000, -17.4295000),
    Landmark("Marche Parcelles", 14.7548000, -17.4315000)
)

@Composable
fun MapScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val agencyPoint = remember { GeoPoint(AppConstants.LATITUDE, AppConstants.LONGITUDE) }
    var currentMode by remember { mutableStateOf(MapMode.SATELLITE) }

    val locationOverlay = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(context), MapView(context)).apply {
            enableMyLocation()
        }
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(SATELLITE_SOURCE)
            setMultiTouchControls(true)
            controller.setZoom(16.0)
            controller.setCenter(agencyPoint)

            overlays.add(CompassOverlay(context, this))

            // Marqueur principal AGENCE
            val agencyMarker = Marker(this).apply {
                position = agencyPoint
                title = "ContactRapide"
                snippet = AppConstants.ADDRESS
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            overlays.add(agencyMarker)

            // Points de repere secondaires
            landmarks.forEach { landmark ->
                val marker = Marker(this).apply {
                    position = GeoPoint(landmark.lat, landmark.lon)
                    title = landmark.name
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                overlays.add(marker)
            }

            overlays.add(locationOverlay)
        }
    }

    LaunchedEffect(currentMode) {
        mapView.setTileSource(
            when (currentMode) {
                MapMode.PLAN -> TileSourceFactory.MAPNIK
                MapMode.SATELLITE -> SATELLITE_SOURCE
            }
        )
        mapView.invalidate()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            locationOverlay.myLocation?.let {
                mapView.controller.animateTo(it)
                mapView.controller.setZoom(17.0)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(NavyDark)) {
        CrTopBar(
            title = "Nous localiser",
            subtitle = AppConstants.ADDRESS,
            onBack = onBack
        )

        Box(modifier = Modifier.fillMaxSize()) {

            AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

            // SELECTEUR 2 MODES
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        MapMode.values().forEach { mode ->
                            val isSelected = mode == currentMode
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(if (isSelected) Navy else Color.Transparent)
                                    .clickable { currentMode = mode }
                                    .padding(horizontal = 24.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = mode.label,
                                    color = if (isSelected) White else Navy,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // BOUTONS FLOTTANTS
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingButton(icon = Icons.Filled.Add, description = "Zoom avant") {
                    mapView.controller.zoomIn()
                }
                FloatingButton(icon = Icons.Filled.Remove, description = "Zoom arriere") {
                    mapView.controller.zoomOut()
                }
                FloatingButton(icon = Icons.Filled.MyLocation, description = "Ma position") {
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationOverlay.myLocation?.let {
                            mapView.controller.animateTo(it)
                            mapView.controller.setZoom(17.0)
                        }
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
                FloatingButton(icon = Icons.Filled.LocationOn, description = "Centrer agence") {
                    mapView.controller.animateTo(agencyPoint)
                    mapView.controller.setZoom(16.0)
                }
            }

            // BARRE DU BAS
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(White)
                    .padding(12.dp)
            ) {
                Text(
                    text = AppConstants.ADDRESS,
                    color = Navy,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Telephone : ${AppConstants.PHONE_DISPLAY}",
                    color = Green,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { IntentUtils.call(context) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Navy)
                    ) {
                        Text("Appeler", color = White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { IntentUtils.openInGoogleMaps(context) },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gold)
                    ) {
                        Text("Google Maps", color = Navy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = Navy,
            modifier = Modifier.size(24.dp)
        )
    }
}
