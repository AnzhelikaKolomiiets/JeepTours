package com.example.jeeptours.domain.usecase
import com.example.jeeptours.domain.model.LocationModel
import com.example.jeeptours.domain.model.SafetyVerdict
import com.example.jeeptours.domain.model.VehicleClass

class CalculateRouteSafetyUseCase {

    fun execute(location: LocationModel, vehicleClass: VehicleClass): SafetyVerdict {
        val diffGap = location.difficulty - vehicleClass.maxAllowedDifficulty

        return when {
            diffGap <= 0 -> {
                val risk = (location.difficulty * 8).coerceAtMost(30)
                SafetyVerdict(
                    riskPercentage = risk,
                    isSafe = true,
                    warningMessage = "Маршрут повністю безпечний для даного класу авто.",
                    recommendation = "Чудовий вибір! Автомобіль легко подолає даний рельєф."
                )
            }
            diffGap in 1..2 -> {
                val risk = 40 + (diffGap * 15)
                SafetyVerdict(
                    riskPercentage = risk,
                    isSafe = true,
                    warningMessage = "Увага! Підвищений ризик через складний рельєф.",
                    recommendation = "Рекомендовано досвідченого водія або підвищити клас авто."
                )
            }
            else -> {
                val risk = (75 + (diffGap * 10)).coerceAtMost(99)
                SafetyVerdict(
                    riskPercentage = risk,
                    isSafe = false,
                    warningMessage = "КРИТИЧНИЙ РИЗИК! Автомобіль недостатньо підготовлений.",
                    recommendation = "Заборонено рух! Оберіть клас Extreme або підготовлений Hard."
                )
            }
        }
    }
}
