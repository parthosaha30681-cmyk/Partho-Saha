package com.psmed.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF123C43)
private val Teal = Color(0xFF087F75)
private val PaleMint = Color(0xFFEAF6F2)
private val Ink = Color(0xFF182A2B)
private val Muted = Color(0xFF6B7C7C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Teal,
                secondary = Navy,
                background = Color(0xFFF7FAF9),
                surface = Color.White
            )) {
                PsMedApp()
            }
        }
    }
}

private data class Medicine(
    val brand: String,
    val generic: String,
    val strength: String,
    val form: String,
    val manufacturer: String,
    val category: String,
    val description: String
)

// Illustrative entries only. Replace with a verified, licensed medicine data source before release.
private val sampleMedicines = listOf(
    Medicine("Demo Paracetamol", "Paracetamol", "500 mg", "Tablet", "Sample manufacturer", "Pain relief", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo Cetirizine", "Cetirizine", "10 mg", "Tablet", "Sample manufacturer", "Allergy", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo Omeprazole", "Omeprazole", "20 mg", "Capsule", "Sample manufacturer", "Gastro", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo ORS", "Oral rehydration salts", "Standard sachet", "Powder", "Sample manufacturer", "Wellness", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo Amoxicillin", "Amoxicillin", "250 mg", "Capsule", "Sample manufacturer", "Antibiotic", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo Amlodipine", "Amlodipine", "5 mg", "Tablet", "Sample manufacturer", "Heart health", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo Metformin", "Metformin", "500 mg", "Tablet", "Sample manufacturer", "Diabetes care", "Example catalog entry for searching by brand or generic name."),
    Medicine("Demo Vitamin C", "Ascorbic acid", "500 mg", "Tablet", "Sample manufacturer", "Vitamins", "Example catalog entry for searching by brand or generic name.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PsMedApp() {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedTab by remember { mutableStateOf(0) }
    var selectedMedicine by remember { mutableStateOf<Medicine?>(null) }
    var favorites by remember { mutableStateOf(setOf<String>()) }

    val categories = listOf("All", "Pain relief", "Allergy", "Gastro", "Wellness", "Antibiotic", "Vitamins")
    val filtered = sampleMedicines.filter { medicine ->
        val matchesQuery = listOf(medicine.brand, medicine.generic, medicine.strength, medicine.form, medicine.manufacturer)
            .any { it.contains(query.trim(), ignoreCase = true) }
        val matchesCategory = selectedCategory == "All" || medicine.category == selectedCategory
        val matchesTab = selectedTab != 1 || medicine.brand in favorites
        matchesQuery && matchesCategory && matchesTab
    }

    Scaffold(
        containerColor = Color(0xFFF7FAF9),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 }, icon = { Icon(Icons.Default.Home, contentDescription = "Home") }, label = { Text("Explore") })
                NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 }, icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") }, label = { Text("Saved") })
                NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 }, icon = { Icon(Icons.Default.Info, contentDescription = "About") }, label = { Text("About") })
            }
        }
    ) { padding ->
        when (selectedTab) {
            2 -> AboutScreen(Modifier.padding(padding))
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(44.dp).background(Teal, CircleShape),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.LocalPharmacy, contentDescription = null, tint = Color.White) }
                        Spacer(Modifier.width(11.dp))
                        Column {
                            Text("PS Med", fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Navy)
                            Text("Medicine information, made simple", fontSize = 12.sp, color = Muted)
                        }
                    }
                }
                item {
                    Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Navy)) {
                        Column(Modifier.fillMaxWidth().padding(20.dp)) {
                            Text(if (selectedTab == 1) "Your saved list" else "Find medicine information", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(6.dp))
                            Text("Search by brand, generic name, strength, or form.", color = Color(0xFFD8E9E7), fontSize = 13.sp)
                            Spacer(Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(14.dp)).padding(horizontal = 13.dp, vertical = 13.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, tint = Teal)
                                Spacer(Modifier.width(9.dp))
                                BasicTextField(
                                    value = query,
                                    onValueChange = { query = it },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = Ink),
                                    decorationBox = { inner ->
                                        if (query.isEmpty()) Text("Try a medicine name…", color = Muted, fontSize = 14.sp)
                                        inner()
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Browse categories", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                        Spacer(Modifier.weight(1f))
                        Text("DEMO DATA", fontSize = 10.sp, color = Teal, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(7.dp))
                    androidx.compose.foundation.lazy.LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category) }
                            )
                        }
                    }
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (selectedTab == 1) "Saved medicines" else "Medicine directory", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                        Spacer(Modifier.weight(1f))
                        Text("${filtered.size} entries", fontSize = 12.sp, color = Muted)
                    }
                }
                if (filtered.isEmpty()) {
                    item { EmptyState(if (selectedTab == 1 && favorites.isEmpty()) "No saved medicines yet" else "No matches found", "Try another search or category.") }
                } else {
                    items(filtered, key = { it.brand }) { medicine ->
                        MedicineCard(
                            medicine = medicine,
                            isFavorite = medicine.brand in favorites,
                            onClick = { selectedMedicine = medicine },
                            onFavorite = {
                                favorites = if (medicine.brand in favorites) favorites - medicine.brand else favorites + medicine.brand
                            }
                        )
                    }
                }
                item {
                    Text("Demo catalog only. Medicine details are not verified and are not medical advice.", fontSize = 11.sp, color = Muted, modifier = Modifier.padding(top = 3.dp))
                }
            }
        }
    }

    selectedMedicine?.let { medicine ->
        AlertDialog(
            onDismissRequest = { selectedMedicine = null },
            title = { Text(medicine.brand, color = Navy, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailLine("Generic", medicine.generic)
                    DetailLine("Strength", medicine.strength)
                    DetailLine("Form", medicine.form)
                    DetailLine("Manufacturer", medicine.manufacturer)
                    DetailLine("Category", medicine.category)
                    Text(medicine.description, color = Muted, fontSize = 13.sp)
                    Text("Demo information only. Ask a qualified healthcare professional about medicines and treatment.", color = Color(0xFF9A5A18), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            },
            confirmButton = { TextButton(onClick = { selectedMedicine = null }) { Text("Done") } }
        )
    }
}

@Composable
private fun MedicineCard(medicine: Medicine, isFavorite: Boolean, onClick: () -> Unit, onFavorite: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(46.dp).background(PaleMint, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.LocalPharmacy, contentDescription = null, tint = Teal)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(medicine.brand, color = Ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(3.dp))
                Text(medicine.generic, color = Muted, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(7.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Tag(medicine.strength)
                    Tag(medicine.form)
                }
            }
            Spacer(Modifier.width(6.dp))
            TextButton(onClick = onFavorite) {
                Icon(Icons.Default.Favorite, contentDescription = if (isFavorite) "Remove from saved" else "Save medicine", tint = if (isFavorite) Color(0xFFD65A64) else Muted, modifier = Modifier.size(22.dp))
            }
        }
    }
}

@Composable
private fun Tag(label: String) {
    Surface(color = PaleMint, shape = RoundedCornerShape(20.dp)) {
        Text(label, modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp), color = Teal, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DetailLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Muted, fontSize = 13.sp)
        Spacer(Modifier.width(12.dp))
        Text(value, color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun EmptyState(title: String, subtitle: String) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Teal, modifier = Modifier.size(30.dp))
            Spacer(Modifier.height(8.dp))
            Text(title, color = Ink, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AboutScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(46.dp).background(Teal, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.LocalPharmacy, contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("About PS Med", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text("A simple medicine directory prototype", color = Muted, fontSize = 13.sp)
            }
        }
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Built for quick lookup", color = Ink, fontWeight = FontWeight.SemiBold)
                Text("Search the demo directory by brand, generic name, strength, or form. Save entries to revisit them later.", color = Muted, fontSize = 14.sp)
                Text("The Google Sheet has not been connected. All medicines currently shown are illustrative sample records, not an authoritative catalog.", color = Color(0xFF9A5A18), fontSize = 13.sp)
            }
        }
        Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = PaleMint)) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Important", color = Navy, fontWeight = FontWeight.Bold)
                Text("This app does not provide medical advice, diagnosis, or treatment recommendations. Always verify medicine information with an official source and consult a qualified healthcare professional.", color = Ink, fontSize = 13.sp)
            }
        }
    }
}
