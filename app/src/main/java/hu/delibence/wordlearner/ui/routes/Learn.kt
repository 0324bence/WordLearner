package hu.delibence.wordlearner.ui.routes

import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import hu.delibence.wordlearner.R
import kotlin.math.roundToInt

@Composable
fun Learn() {
    //Log.d("Debug Log", LocalContext.current.filesDir.path.toString())
    var wordRevealed by remember { mutableStateOf(false) }

    var cardOffsetX by remember { mutableFloatStateOf(0f) }
    var cardRotation by remember { mutableFloatStateOf(0f) }

    val cardOffsetXTransition by updateTransition(targetState = cardOffsetX, label = "").animateFloat(label = "") { it }
    val cardRotationTransition by updateTransition(targetState = cardRotation, label = "").animateFloat(label = "") { it }

    fun nextWord(known: Boolean) {
        Log.d("Debug Log", "Next word")
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(10f.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0x66E52626),
                            Color.Transparent
                        )
                    )
                )
                .zIndex(5f)
        )
        Card(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth(0.8f)
                .zIndex(4f)
                .offset { IntOffset(cardOffsetXTransition.roundToInt(), 0) }
                .rotate(cardRotationTransition)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState {
                        cardOffsetX += it
                        cardRotation = if (it > 0f) 3f else -3f
                    },
                    onDragStopped = {
                        if (cardOffsetX > 100f) nextWord(true) else if (cardOffsetX < -100f) nextWord(false)
                        cardOffsetX = 0f
                        cardRotation = 0f
                    }
                ),

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2f.dp),
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(5f.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = when {
                            (cardOffsetX > 100f) -> Color(0x0D6EE843)
                            (cardOffsetX < -100f) -> Color(0x0DE52626)
                            else -> Color.Transparent
                        }
                    )
                    .padding(10f.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.weight(weight = 9f, fill = true), verticalArrangement = Arrangement.spacedBy(5f.dp)) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 3f, fill = true), verticalArrangement = Arrangement.Center) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                Text(/*"[${currentWord.priority}]"*/ "[]", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                Text(
                                    /*if (settings.langToLearn == LanguageToLearn.Lang1) currentWord.lang2 else currentWord.lang1,*/
                                    "Alma",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1f, fill = true)) {
                            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                                Box(modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2f.dp)
                                    .background(color = MaterialTheme.colorScheme.secondary)) {}
                            }

                        }
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 3f, fill = true), verticalArrangement = Arrangement.Center) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                Text(
                                    text = if (wordRevealed) "Apple" else "***",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(weight = 3f, fill = true), verticalArrangement = Arrangement.SpaceBetween) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Button(
                                onClick = { wordRevealed = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                elevation = ButtonDefaults.elevatedButtonElevation()
                            ) {
                                Text(text = stringResource(id = R.string.learn_show))
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            //Dont knowFAB
                            FloatingActionButton(
                                onClick = {
                                    nextWord(false)
                                },
                                containerColor = Color(0xFF633B48),
                                contentColor = MaterialTheme.colorScheme.tertiary
                            ) {
                                Icon(imageVector = Icons.Outlined.Close, contentDescription = "Delete fab button")
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Outlined.Flag, contentDescription = "Flag icon")
                            }
                            FloatingActionButton(
                                onClick = { nextWord(true) },
                                containerColor = Color(0xFF43633B),
                                contentColor = MaterialTheme.colorScheme.tertiary
                            ) {
                                Icon(imageVector = Icons.Outlined.Check, contentDescription = "Delete fab button")
                            }
                        }
                    }
                }
            }
        }
        /*Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Text(stringResource(id = R.string.learn_no_words), color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.headlineLarge)
        }*/
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(10f.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x666EE843)

                        )
                    )
                )
                .zIndex(5f)
        )
    }
}