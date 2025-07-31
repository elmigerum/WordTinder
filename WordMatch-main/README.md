# WordMatch 📱
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![Material Design 3](https://img.shields.io/badge/Design-Material%203-orange.svg)](https://m3.material.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> Современное Android-приложение для изучения английских слов с интуитивным Tinder-подобным интерфейсом

## 👨‍💻 Автор

**Elmigerum, me**
- GitHub: [@elmigerum](https://github.com/elmigerum)
- GitHub: [@danissimoae](https://github.com/danissimoae)

<img width="881" height="600" alt="image" src="https://github.com/user-attachments/assets/c7842fc3-8816-4e47-9da5-ae9519f56941" />
<img width="729" height="778" alt="image" src="https://github.com/user-attachments/assets/c3fa987a-6f72-497f-ab17-8e504baff53a" />



## 🎯 Описание

**WordMatch** — это инновационное мобильное приложение для изучения английского языка, которое превращает скучное заучивание слов в увлекательный процесс. Используя знакомый многим интерфейс свайпов, приложение делает обучение интерактивным и эффективным.

### ✨ Ключевые особенности

- 🔄 **Tinder-подобный интерфейс**: Свайп влево — "не знаю", свайп вправо — "знаю"
- 🎨 **Современный дизайн**: Material Design 3 с минималистичной цветовой схемой
- 📊 **Система повторения**: Автоматическое добавление незнакомых слов в список для повторения
- 🎯 **Прогресс-трекинг**: Визуальное отображение прогресса изучения
- 📱 **Отзывчивый UI**: Плавные анимации и переходы
- 🎮 **Геймификация**: Интерактивные элементы и достижения

## 🚀 Технологии

### Основной стек
- **Kotlin** — современный язык программирования для Android
- **Jetpack Compose** — декларативный UI-фреймворк от Google
- **Material Design 3** — последняя версия дизайн-системы Google
- **Android Architecture Components** — MVVM паттерн с ViewModel

### Библиотеки и зависимости
```kotlin
dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    
    // Jetpack Compose
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    
    // Architecture Components
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
}
```

## 🏗️ Архитектура

Приложение построено с использованием современных Android-практик:

```
app/
├── src/main/java/com/example/wordmatch/
│   ├── MainActivity.kt                 # Точка входа приложения
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── LearningScreen.kt      # Основной экран обучения
│   │   │   ├── ReviewScreen.kt        # Экран повторения
│   │   │   └── CompletionScreen.kt    # Экран завершения
│   │   ├── components/
│   │   │   ├── SwipeableWordCard.kt   # Свайпающиеся карточки
│   │   │   └── SwipeIndicator.kt      # Индикаторы свайпов
│   │   └── theme/
│   │       └── Theme.kt               # Material Design 3 тема
│   ├── data/
│   │   ├── model/
│   │   │   └── Word.kt                # Модель данных слова
│   │   └── repository/
│   │       └── WordRepository.kt      # Управление данными
│   └── viewmodel/
│       └── WordLearningViewModel.kt   # Бизнес-логика
```

### MVVM Pattern
- **Model**: Данные и бизнес-логика (`Word`, `WordRepository`)
- **View**: UI-компоненты (Compose UI)
- **ViewModel**: Связующее звено между Model и View

## 🎮 Функциональность

### Основные экраны

<img width="881" height="600" alt="image" src="https://github.com/user-attachments/assets/d5d28b19-cba0-439c-82b6-89d39d259efc" />
<img width="729" height="778" alt="image" src="https://github.com/user-attachments/assets/3de62bd6-d878-4cd3-8a4a-346a57e35842" />



#### 🏠 Экран обучения
- Отображение карточек со словами
- Свайп-жесты для оценки знания слова
- Кнопки для альтернативного выбора
- Прогресс-бар с текущим прогрессом

#### 📚 Экран повторения
- Список слов, требующих повторения
- Детальная информация о каждом слове
- Примеры использования

#### 🎉 Экран завершения
- Статистика прохождения
- Возможность начать заново
- Переход к повторению

### Интерактивные элементы

```kotlin
// Пример реализации свайп-жестов
.pointerInput(Unit) {
    detectDragGestures(
        onDragEnd = {
            when {
                offsetX > swipeThreshold -> onSwipeRight()
                offsetX < -swipeThreshold -> onSwipeLeft()
                else -> snapBackToCenter()
            }
        }
    ) { change, dragAmount ->
        offsetX += dragAmount.x
        offsetY += dragAmount.y
    }
}
```

## 🛠️ Установка и запуск

### Требования
- **Android Studio**: Arctic Fox | 2020.3.1 или новее
- **Android SDK**: API уровень 24 (Android 7.0) или выше
- **JDK**: версия 8 или выше
- **Gradle**: 7.0 или выше

### Инструкция по установке

1. **Клонирование репозитория**
   ```bash
   git clone https://github.com/danissimoae/wordmatch.git
   cd wordmatch
   ```

2. **Открытие в Android Studio**
   - File → Open
   - Выберите папку проекта
   - Дождитесь синхронизации Gradle

3. **Настройка проекта**
   - Убедитесь, что выбран правильный SDK
   - Синхронизируйте проект (Sync Now)

4. **Запуск приложения**
   - Подключите Android-устройство или запустите эмулятор
   - Нажмите Run (▶️) или `Shift + F10`

### Конфигурация сборки

```kotlin
android {
    compileSdk = 35
    
    defaultConfig {
        applicationId = "com.example.wordmatch"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
```

## 🔄 Состояние приложения

Приложение управляет следующими состояниями:

- **Текущее слово**: Индекс активной карточки
- **Изученные слова**: Список пройденных слов
- **Слова для повторения**: Незнакомые слова
- **Прогресс**: Процент завершения
- 

## 📄 Лицензия

Этот проект лицензирован под MIT License - подробности в файле [LICENSE](LICENSE).

```
MIT License

Copyright (c) 2025 WordMatch

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```


⭐ **Если проект оказался полезным, поставьте звездочку!** ⭐

