// MainActivity.kt
package com.example.wordmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordLearningTheme {
                WordLearningApp()
            }
        }
    }
}

@Composable
fun WordLearningTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4CAF50),
            secondary = Color(0xFF81C784),
            surface = Color.White,
            background = Color(0xFFF8F9FA),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onSurface = Color(0xFF2E2E2E),
            onBackground = Color(0xFF2E2E2E)
        ),
        content = content
    )
}

data class Word(
    val id: Int,
    val english: String,
    val russian: String,
    val example: String
)

class WordLearningViewModel : ViewModel() {
    private val _words = mutableStateListOf<Word>()
    private val _reviewWords = mutableStateListOf<Word>()
    private val _currentWordIndex = mutableStateOf(0)

    val words: SnapshotStateList<Word> = _words
    val reviewWords: SnapshotStateList<Word> = _reviewWords
    val currentWordIndex: Int by _currentWordIndex

    init {
        loadInitialWords()
    }

    private fun loadInitialWords() {
        val initialWords = listOf(
            Word(1, "Beautiful", "Красивый", "She is very beautiful."),
            Word(2, "Adventure", "Приключение", "Life is an adventure."),
            Word(3, "Knowledge", "Знание", "Knowledge is power."),
            Word(4, "Freedom", "Свобода", "Freedom is precious."),
            Word(5, "Happiness", "Счастье", "Happiness comes from within."),
            Word(6, "Challenge", "Вызов", "This is a real challenge."),
            Word(7, "Success", "Успех", "Success requires hard work."),
            Word(8, "Courage", "Смелость", "It takes courage to try."),
            Word(9, "Dream", "Мечта", "Follow your dreams."),
            Word(10, "Journey", "Путешествие", "Life is a journey.")
        )
        _words.addAll(initialWords)
    }

    fun getCurrentWord(): Word? {
        return if (_currentWordIndex.value < _words.size) {
            _words[_currentWordIndex.value]
        } else null
    }

    fun swipeLeft() {
        getCurrentWord()?.let { word ->
            _reviewWords.add(word)
        }
        nextWord()
    }

    fun swipeRight() {
        nextWord()
    }

    private fun nextWord() {
        _currentWordIndex.value++
    }

    fun resetProgress() {
        _currentWordIndex.value = 0
        _reviewWords.clear()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordLearningApp() {
    val viewModel: WordLearningViewModel = viewModel()
    var currentScreen by remember { mutableStateOf("learning") }

    when (currentScreen) {
        "learning" -> LearningScreen(
            viewModel = viewModel,
            onShowReview = { currentScreen = "review" }
        )
        "review" -> ReviewScreen(
            reviewWords = viewModel.reviewWords,
            onBack = { currentScreen = "learning" }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningScreen(
    viewModel: WordLearningViewModel,
    onShowReview: () -> Unit
) {
    val currentWord = viewModel.getCurrentWord()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "WordLearning",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(onClick = onShowReview) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "Review Words",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (currentWord != null) {
                SwipeableWordCard(
                    word = currentWord,
                    onSwipeLeft = { viewModel.swipeLeft() },
                    onSwipeRight = { viewModel.swipeRight() }
                )

                // Progress indicator
                LinearProgressIndicator(
                    progress = (viewModel.currentWordIndex.toFloat() / viewModel.words.size),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                CompletionScreen(
                    reviewWordsCount = viewModel.reviewWords.size,
                    onRestart = { viewModel.resetProgress() },
                    onShowReview = onShowReview
                )
            }
        }
    }
}

@Composable
fun SwipeableWordCard(
    word: Word,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    val swipeThreshold = with(density) { 150.dp.toPx() }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(400.dp)
            .graphicsLayer {
                translationX = offsetX
                translationY = offsetY
                rotationZ = offsetX / 20f
                alpha = 1f - (abs(offsetX) / (swipeThreshold * 2))
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            when {
                                offsetX > swipeThreshold -> {
                                    // Animate out to the right
                                    val animateX = Animatable(offsetX)
                                    animateX.animateTo(
                                        targetValue = swipeThreshold * 3,
                                        animationSpec = tween(200)
                                    )
                                    onSwipeRight()
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                                offsetX < -swipeThreshold -> {
                                    // Animate out to the left
                                    val animateX = Animatable(offsetX)
                                    animateX.animateTo(
                                        targetValue = -swipeThreshold * 3,
                                        animationSpec = tween(200)
                                    )
                                    onSwipeLeft()
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                                else -> {
                                    // Snap back to center
                                    val animateX = Animatable(offsetX)
                                    val animateY = Animatable(offsetY)
                                    launch {
                                        animateX.animateTo(0f, animationSpec = tween(200))
                                    }
                                    launch {
                                        animateY.animateTo(0f, animationSpec = tween(200))
                                    }
                                    offsetX = animateX.value
                                    offsetY = animateY.value
                                }
                            }
                        }
                    }
                ) { change, dragAmount ->
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                Text(
                    text = word.english,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = word.russian,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        text = word.example,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Swipe indicators
            if (offsetX > 50) {
                SwipeIndicator(
                    text = "ЗНАЮ",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            } else if (offsetX < -50) {
                SwipeIndicator(
                    text = "НЕ ЗНАЮ",
                    color = Color(0xFFFF5722),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
    }

    // Manual swipe buttons
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 450.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FloatingActionButton(
            onClick = onSwipeLeft,
            containerColor = Color(0xFFFF5722),
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Don't Know",
                tint = Color.White
            )
        }

        FloatingActionButton(
            onClick = onSwipeRight,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                Icons.Default.Done,
                contentDescription = "Know",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SwipeIndicator(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .rotate(if (text == "ЗНАЮ") -15f else 15f),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun CompletionScreen(
    reviewWordsCount: Int,
    onRestart: () -> Unit,
    onShowReview: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "🎉",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Отлично!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Вы прошли все слова",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        if (reviewWordsCount > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Слов для повтора: $reviewWordsCount",
                fontSize = 16.sp,
                color = Color(0xFFFF5722)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Начать сначала", fontSize = 16.sp)
        }

        if (reviewWordsCount > 0) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onShowReview,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Показать слова для повтора", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    reviewWords: List<Word>,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Слова для повтора") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (reviewWords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎯",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Отлично!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Нет слов для повтора",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reviewWords) { word ->
                    ReviewWordCard(word = word)
                }
            }
        }
    }
}

@Composable
fun ReviewWordCard(word: Word) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = word.english,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = word.russian,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = word.example,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}