package com.example.jeeptours.domain

class CalculateRouteSafetyUseCase {
    operator fun invoke(location: LocationModel, vehicle: VehicleClass): SafetyVerdict {
        val diffGap = location.difficulty - vehicle.maxAllowedDifficulty

        val riskPercentage = when {
            diffGap <= -3 -> 10
            diffGap == -2 -> 25
            diffGap == -1 -> 40
            diffGap == 0 -> 55
            diffGap == 1 -> 75
            diffGap == 2 -> 90
            else -> 99
        }

        val isSafe = riskPercentage < 75

        val warningMessage = when {
            riskPercentage < 30 -> "Маршрут цілком безпечний для даного авто."
            riskPercentage < 60 -> "Помірний ризик. Потрібен досвідчений водій."
            riskPercentage < 80 -> "Високий ризик! Можливе застрягання в болоті/снігу."
            else -> "КРИТИЧНИЙ РИЗИК! Автомобіль не відповідає складності гори."
        }

        val recommendation = if (isSafe) {
            "Чудовий вибір! Можна вирушати в тур."
        } else {
            "Рекомендуємо обрати вищий клас авто (наприклад, EXTREME) або менш складний маршрут."
        }

        return SafetyVerdict(
            riskPercentage = riskPercentage,
            isSafe = isSafe,
            warningMessage = warningMessage,
            recommendation = recommendation
        )
    }
}