package hu.delibence.wordlearner

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import hu.delibence.wordlearner.ui.MainScreen
import hu.delibence.wordlearner.ui.theme.WordLearnerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            WordLearnerTheme {
                MainScreen()
            }
        }
    }
}

fun Modifier.onTouchHeld(
    pollDelay: Duration,
    onTouchHeld: (timeElapsed: Duration) -> Unit
) = composed {
    val scope = rememberCoroutineScope()
    pointerInput(onTouchHeld) {
        awaitEachGesture {
            val initialDown = awaitFirstDown(requireUnconsumed = false)
            val initialDownTime = System.nanoTime()
            val initialTouchHeldJob = scope.launch {
                while (initialDown.pressed) {
                    val timeElapsed = System.nanoTime() - initialDownTime
                    onTouchHeld(timeElapsed.nanoseconds)
                    delay(pollDelay)
                }
            }
            waitForUpOrCancellation()
            initialTouchHeldJob.cancel()
        }
    }
}
