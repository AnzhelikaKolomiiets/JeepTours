package com.example.jeeptours.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jeeptours.domain.VehicleClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(viewModel: OrderViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Carpathian Jeep Tours AI") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("1. Оберіть маршрут:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            state.availableLocations.forEach { location ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = (state.selectedLocation?.id == location.id),
                        onClick = { viewModel.selectLocation(location) }
                    )
                    Text("${location.name} (Складність: ${location.difficulty}/10)")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("2. Оберіть клас авто:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            VehicleClass.values().forEach { vehicle ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = (state.selectedVehicle == vehicle),
                        onClick = { viewModel.selectVehicle(vehicle) }
                    )
                    Text("${vehicle.title} (до складн. ${vehicle.maxAllowedDifficulty})")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.calculateTrip() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Розрахувати безпеку та вартість")
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.verdict?.let { verdict ->
                val cardBg = if (verdict.isSafe) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                val textColor = if (verdict.isSafe) Color(0xFF2E7D32) else Color(0xFFC62828)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBg)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (verdict.isSafe) "БЕЗПЕЧНО (${verdict.riskPercentage}% ризику)" else "УВАГА! НЕБЕЗПЕЧНО (${verdict.riskPercentage}% ризику)",
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(verdict.warningMessage)
                        Text(verdict.recommendation, fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Орієнтовна вартість: ${state.calculatedCost} грн", fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.saveCurrentOrder() },
                            enabled = !state.isSaved,
                            colors = ButtonDefaults.buttonColors(containerColor = textColor)
                        ) {
                            Text(if (state.isSaved) "Збережено у БД ✓" else "Зберегти замовлення у Room DB")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Історія збережених замовлень (Room DB):", fontWeight = FontWeight.Bold)

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(state.savedOrders) { order ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(order.locationName, fontWeight = FontWeight.Bold)
                                Text(order.vehicleClassTitle, fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${order.totalCost} грн", fontWeight = FontWeight.Bold)
                                Text("Ризик: ${order.riskPercentage}%", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}