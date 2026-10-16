 Carpathian Jeep Tours AI 🏔️🚙

Android-додаток (Kotlin, Jetpack Compose, Room DB): вибір гірських маршрутів Карпат + клас позашляховика + інтелектуальний AI Safety Engine + калькулятор вартості туру + локальне збереження замовлень[cite: 1]. Пакет: `com.example.jeeptours`.

## З чого почати

| Мета | Файл |
| :--- | :--- |
| **Зрозуміти проєкт** | `concept.md` |
| **Що готово, що ні, критерії готовності** | `requirements.md` |
| **Як влаштований код і потоки** | `architecture.md` |
| **Формат AI-команд і модель даних** | `api-contract.md` |
| **Як працює AI-частина** | `ai-component.md` |
| **Як перевірити** | `testing.md` |
| **Що зламано чи ризиковано** | `known-limitations.md` |
| **Чому зроблено саме так** | `decisions.md` |
| **Правила для кодінг-агента** | `ai-instructions.md` |
| **Історія змін** | `CHANGELOG.md` |

Порядок читання для нового розробника або агента: огляд (`concept.md`) → архітектура (`architecture.md`) → вимоги → відомі обмеження → ai-інструкції.

## Налаштування

`app/src/main/AndroidManifest.xml`, перед `<application>`:
```xml
<uses-permission android:name="android.permission.INTERNET" />

```

`app/build.gradle.kts`, залежності для хмарного та клієнтського AI (версії звіряти з документацією Firebase / Android SDK):

```kotlin
implementation(platform("com.google.firebase:firebase-bom:<версія>"))
implementation("com.google.firebase:firebase-ai")
implementation("com.google.firebase:firebase-appcheck-debug")

```

Для хмарного AI: `google-services.json` у `app/` і плагін `com.google.gms.google-services`. Файл не комітити. Без цього додаток працює, а AI обмежується локальними правилами AI Safety Engine.

Запуск: `./gradlew assembleDebug`, потім запустити на реальному пристрої або емуляторі (AOSP / AVD API 29+). Версії `minSdk`, `compileSdk`, Kotlin і Compose дивитися в `app/build.gradle.kts`.

## Ключові факти

| Параметр | Значення |
| --- | --- |
| **ОС / SDK** | Android 10+ (minSdk 29)

|
| **Потоки** | UI (Compose), DB-operations (`Dispatchers.IO`), AI calculation (`viewModelScope`)

|
| **Файловий код** | `MainActivity.kt`, `OrderScreen.kt`, `OrderViewModel.kt`, `CalculateRouteSafetyUseCase.kt`, `CalculateTripCostUseCase.kt`, `AppDatabase.kt`, `OrderDao.kt`, `OrderRepository.kt`<br> |
| **Бекенд** | Немає (Client-Side AI Engine + Room DB); заплановано Firebase (Auth, Firestore, Storage)

|
| **Автоматичні тести** | Немає (заплановано JUnit5 / Robolectric)

|

## Словник

* **LocationModel**: модель маршруту в Карпатах (включає рівень складності `difficulty` від 1 до 10, тривалість у годинах, висоту над рівнем моря).


* **VehicleClass**: клас позашляховика (`LIGHT`, `HARD`, `EXTREME`) із визначеною максимальною допустимою складністю рельєфу та базовим годинним тарифом.


* **SafetyVerdict**: об'єкт-результат роботи AI Engine, що містить відсоток ризику (`riskPercentage`), статус безпеки (`isSafe`), застереження та пораду щодо вибору авто.


* **AI Safety Engine (`CalculateRouteSafetyUseCase`)**: бізнес-логіка (клієнтський AI-шар), яка порівнює складність рельєфу гори та технічні можливості авто для розрахунку ймовірності застрягання/аварійності.


* **TripOrderEntity**: сутність локальної бази даних Room DB для збереження обраного туру, його вартості та вердикту ризику.


* **OrderUiState**: єдине джерело правди стану інтерфейсу (включає список маршрутів, обрані параметри, вердикт та історію збережених турів).



```

```