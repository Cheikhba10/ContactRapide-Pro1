package com.contactrapide.app.feature.map

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color as AndroidColor
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

private val SATELLITE_SOURCE = XYTileSource(
    "EsriWorldImagery",
    1, 19, 256, ".jpg",
    arrayOf("https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}")
)

enum class MapMode(val label: String) {
    PLAN("Plan"),
    SATELLITE("Satellite")
}

private data class Landmark(
    val name: String,
    val description: String,
    val lat: Double,
    val lon: Double
)

private val landmarks = listOf(
    Landmark("Rond-point Parcelles", "Carrefour principal", 14.7543000, -17.4300000),
    Landmark("Station Total", "Station essence 24h", 14.7560000, -17.4295000),
    Landmark("Marche Parcelles", "Marche quotidien", 14.7548000, -17.4315000)
)

private val routeToAgency = listOf(
    GeoPoint(14.7543000, -17.4300000),
    GeoPoint(14.7545000, -17.4302000),
    GeoPoint(14.7548000, -17.4304000),
    GeoPoint(14.7550000, -17.4305500),
    GeoPoint(14.7551443, -17.4306665)
)

private val coverageZone = listOf(
    GeoPoint(14.7570000, -17.4330000),
    GeoPoint(14.7570000, -17.4280000),
    GeoPoint(14.7530000, -17.4280000),
    GeoPoint(14.7530000, -17.4330000)
)

private fun distanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c
}

@Composable
fun MapScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val agencyPoint = remember { GeoPoint(AppConstants.LATITUDE, AppConstants.LONGITUDE) }
    var currentMode by remember { mutableStateOf(MapMode.SATELLITE) }
    var showPolygon by remember { mutableStateOf(true) }
    var showRoute by remember { mutableStateOf(true) }
    var distanceKmValue by remember { mutableStateOf<Double?>(null) }

    val locationOverlay = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(context), MapView(context)).apply {
            enableMyLocation()
        }
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(SATELLITE_SOURCE)
            setMultiTouchControls(true)
            controller.setZoom(16.5)
            controller.setCenter(agencyPoint)

            // IMPORTANT : renderer en software pour que le clip circulaire marche
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)

            overlays.add(CompassOverlay(context, this))

            val polygon = Polygon(this).apply {
                points = coverageZone
                fillColor = AndroidColor.argb(40, 30, 122, 60)
                outlineColor = AndroidColor.rgb(30, 122, 60)
                title = "Zone de couverture"
            }
            overlays.add(polygon)

            val polyline = Polyline(this).apply {
                setPoints(routeToAgency)
                color = AndroidColor.rgb(242, 194, 48)
                width = 6f
                title = "Itineraire"
            }
            overlays.add(polyline)

            landmarks.forEach { landmark ->
                val marker = Marker(this).apply {
                    position = GeoPoint(landmark.lat, landmark.lon)
                    title = landmark.name
                    snippet = landmark.description
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                overlays.add(marker)
            }

            val eventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    if (p != null) {
                        val d = distanceKm(p.latitude, p.longitude, AppConstants.LATITUDE, AppConstants.LONGITUDE)
                        Toast.makeText(
                            context,
                            "Distance agence : ${"%.2f".format(d)} km",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return true
                }
                override fun longPressHelper(p: GeoPoint?): Boolean = false
            })
            overlays.add(eventsOverlay)

            overlays.add(locationOverlay)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            locationOverlay.myLocation?.let { loc ->
                distanceKmValue = distanceKm(
                    loc.latitude, loc.longitude,
                    AppConstants.LATITUDE, AppConstants.LONGITUDE
                )
            }
            kotlinx.coroutines.delay(3000)
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

    LaunchedEffect(showPolygon) {
        mapView.overlays.filterIsInstance<Polygon>().forEach { it.isEnabled = showPolygon }
        mapView.invalidate()
    }

    LaunchedEffect(showRoute) {
        mapView.overlays.filterIsInstance<Polyline>().forEach { it.isEnabled = showRoute }
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Navy, NavyDark)
                    )
                )
        ) {

            // ============== GLOBE CENTRAL ==============
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp, bottom = 200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // SELECTEUR MODE
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MapMode.values().forEach { mode ->
                        val isSelected = mode == currentMode
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (isSelected) Gold else White.copy(alpha = 0.2f))
                                .clickable { currentMode = mode }
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = mode.label,
                                color = if (isSelected) Navy else White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // GLOBE
                Box(contentAlignment = Alignment.Center) {
                    // Lueur exterieure (halo)
                    Box(
                        modifier = Modifier
                            .size(340.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Gold.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    // Cadre annulaire
                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape)
                            .border(width = 4.dp, color = Gold, shape = CircleShape)
                    )
                    // La carte en cercle
                    Box(
                        modifier = Modifier
                            .size(290.dp)
                            .clip(CircleShape)
                    ) {
                        AndroidView(
                            factory = { mapView },
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                    // Marqueur central (icone agence par-dessus)
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Gold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = "Agence",
                            tint = Navy,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "AGENCE CONTACTRAPIDE",
                    color = Gold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = AppConstants.ADDRESS,
                    color = White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 40.dp)
                )

                if (distanceKmValue != null) {
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Gold.copy(alpha = 0.2f))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Vous etes a ${"%.1f".format(distanceKmValue)} km",
                            color = Gold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ============== BOUTONS FLOTTANTS (droite) ==============
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CircleIconButton(Icons.Filled.Add, "Zoom avant") {
                    mapView.controller.zoomIn()
                }
                CircleIconButton(Icons.Filled.Remove, "Zoom arriere") {
                    mapView.controller.zoomOut()
                }
                CircleIconButton(Icons.Filled.MyLocation, "Ma position") {
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        locationOverlay.myLocation?.let {
                            mapView.controller.animateTo(it)
                            mapView.controller.setZoom(17.0)
                        } ?: Toast.makeText(context, "Recherche GPS...", Toast.LENGTH_SHORT).show()
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            }

            // ============== TOGGLES (gauche) ==============
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ToggleChip(label = "Zone", active = showPolygon, onClick = { showPolygon = !showPolygon })
                ToggleChip(label = "Route", active = showRoute, onClick = { showRoute = !showRoute })
            }

            // ============== BARRE DU BAS ==============
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(White)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = Navy,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = AppConstants.ADDRESS,
                        color = Navy,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Telephone : ${AppConstants.PHONE_DISPLAY}",
                    color = Green,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { IntentUtils.call(context) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Navy)
                    ) {
                        Text("Appeler", color = White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { IntentUtils.openInGoogleMaps(context) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Gold)
                    ) {
                        Text("Itineraire", color = Navy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(White.copy(alpha = 0.95f))
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

@Composable
private fun ToggleChip(label: String, active: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) Green else White.copy(alpha = 0.95f))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (active) White else Navy,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
