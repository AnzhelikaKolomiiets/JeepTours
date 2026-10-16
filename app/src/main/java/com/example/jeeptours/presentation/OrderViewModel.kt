package com.example.jeeptours.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jeeptours.data.AppDatabase
import com.example.jeeptours.data.OrderRepository
import com.example.jeeptours.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class OrderUiState(
    val availableLocations: List<LocationModel> = listOf(
        LocationModel("1", "Маковиця (Яремче)", difficulty = 2, durationHours = 2, heightMeters = 984),
        LocationModel("2", "Стежки Драгобрату", difficulty = 6, durationHours = 4, heightMeters = 1700),
        LocationModel("3", "Піп Іван (Чорногора)", difficulty = 9, durationHours = 8, heightMeters = 2028)
    ),
    val selectedLocation: LocationModel? = null,
    val selectedVehicle: VehicleClass = VehicleClass.LIGHT,
    val verdict: SafetyVerdict? = null,
    val calculatedCost: Int = 0,
    val isSaved: Boolean = false,
    val savedOrders: List<TripOrder> = emptyList()
)

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderRepository(AppDatabase.getDatabase(application).orderDao())
    private val calculateSafetyUseCase = CalculateRouteSafetyUseCase()
    private val calculateCostUseCase = CalculateTripCostUseCase()

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(selectedLocation = it.availableLocations.first()) }

        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllOrders().collect { orders ->
                _uiState.update { it.copy(savedOrders = orders) }
            }
        }
    }

    fun selectLocation(location: LocationModel) {
        _uiState.update { it.copy(selectedLocation = location, verdict = null, isSaved = false) }
    }

    fun selectVehicle(vehicle: VehicleClass) {
        _uiState.update { it.copy(selectedVehicle = vehicle, verdict = null, isSaved = false) }
    }

    fun calculateTrip() {
        val loc = _uiState.value.selectedLocation ?: return
        val veh = _uiState.value.selectedVehicle

        val verdict = calculateSafetyUseCase(loc, veh)
        val cost = calculateCostUseCase(loc, veh)

        _uiState.update {
            it.copy(verdict = verdict, calculatedCost = cost, isSaved = false)
        }
    }

    fun saveCurrentOrder() {
        val state = _uiState.value
        val loc = state.selectedLocation ?: return
        val verdict = state.verdict ?: return

        val order = TripOrder(
            locationName = loc.name,
            vehicleClassTitle = state.selectedVehicle.title,
            riskPercentage = verdict.riskPercentage,
            totalCost = state.calculatedCost
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.saveOrder(order)
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}